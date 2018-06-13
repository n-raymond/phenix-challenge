package phenix.dataProcessors.topSalesAggregation
import java.time.LocalDate
import java.util.UUID

import com.typesafe.config.ConfigFactory
import phenix.dataFiles.DataFileService
import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ProductQuantity, ProductValue}
import phenix.utils.{ResourceCloseable, SuccessFilter}

class LinearTopSalesAggregator(dataFileService: DataFileService)
    extends TopSalesAggregator
        with ResourceCloseable
        with SuccessFilter {

    private val conf = ConfigFactory.load()

    private val groupSize = conf.getInt("transactions.group-size")

    /**
      * Divides the datafile Iterable in entry to get several groups of file that can
      * be Aggregated independently. Then, reduce all the specific results to obtain
      * a the top product of each shop.
      * @param productQuantities An iterable of ProductQuantity readable files that group
      *                          for each product his value for a specific shop
      * @return An iterable of the resulting files
      */
    override def aggregate(productQuantities: Iterable[(Int, ReadableDataFile[ProductQuantity])]): Iterable[(UUID, ReadableDataFile[ProductValue])] = {
        val groups = productQuantities.grouped(groupSize).zipWithIndex

        val intermediates = groups.map { case (group, groupId) =>
            intermediateAggragation(groupId, group)
        }

        intermediates reduce reducer
    }

    /**
      * Does the intermediate aggregation for a group of productQuantity files.
      * Foreach group, it will aggregate all the productquantities by shop in a map.
      * This map will then be used to create intermediate files to save this state
      * on the filesystem.
      * @param groupId The id of the group
      * @param fileGroup The group of files.
      * @return An iterable of couples representing (the shopId, an intermediate file)
      */
    def intermediateAggragation(groupId: Int, fileGroup: Iterable[(Int, ReadableDataFile[ProductQuantity])]) : Iterable[(UUID, ReadableDataFile[ProductValue])] = {
        val initialMap = Map[UUID, List[ProductValue]]()
        val date = retrieveDateInFirstFile(fileGroup)

        val aggregations = (initialMap /: fileGroup) {
            case (acc, (productId, productQtyFile)) => tryWith(productQtyFile) { file =>
                val content = filterSuccessValues(file.getContent)
                aggregateProductQuantitiesByShop(content, productId, acc)
            }
        }

        aggregations map { case (shop, productValues) =>
            val sorted = retrieveTop100(productValues)

            tryWith(dataFileService.getIntermediateShopTopSellsWriter(shop, groupId, date)) {
                _.writeData(sorted)
            }

            (shop, dataFileService.getIntermediateShopTopSellsReader(shop, groupId, date))
        }
    }



    /**
      * Takes an Iterable of ProductQuantity, aggregate them by shop, and add this aggregation to the given map.
      * @param productQuantities The Iterable of ProductQuantity
      * @param productId The product Id
      * @param map The map in which must be added the resulting product values
      * @return The resulting map with the fresh aggregation
      */
    def aggregateProductQuantitiesByShop(
                                            productQuantities: Iterable[ProductQuantity],
                                            productId: Int,
                                            map: Map[UUID, List[ProductValue]]
                                        ): Map[UUID, List[ProductValue]] = {
        (map /: productQuantities) {
            case (acc, productQty) =>
                val list = acc.getOrElse(productQty.shop, List())
                acc + (productQty.shop -> (new ProductValue(productId, productQty.quantity) :: list))
        }
    }

    def reducer(a: Iterable[(UUID, ReadableDataFile[ProductValue])], b: Iterable[(UUID, ReadableDataFile[ProductValue])]) : Iterable[(UUID, ReadableDataFile[ProductValue])] = {
        def merged = (a ++ b).groupBy (_._1)
        def date = retrieveDateInFirstFile(a)

        merged map { case (uuid, files) =>
            val data = files.flatMap(_._2.getContent)
            val processedData = retrieveTop100(filterSuccessValues(data))
            tryWith(dataFileService.getShopTopSellsWriter(uuid, date)) {
                _.writeData(processedData)
            }
            (uuid, dataFileService.getShopTopSellsReader(uuid, date))
        }
    }

    /**
      * Get the date of the first file in the list
      * @param fileGroup the list
      * @return a local date
      */
    def retrieveDateInFirstFile(fileGroup: Iterable[(_, ReadableDataFile[_])]): LocalDate = {
        fileGroup.head._2.date
    }

    def retrieveTop100(values: Iterable[ProductValue]): Iterable[ProductValue] = {
        values.toList.sortBy(- _.value).take(100)
    }


}
