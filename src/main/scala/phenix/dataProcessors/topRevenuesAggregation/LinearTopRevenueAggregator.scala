package phenix.dataProcessors.topRevenuesAggregation

import java.time.LocalDate
import java.util.UUID

import com.typesafe.config.ConfigFactory
import phenix.dataFiles.DataFileService
import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ProductPrice, ProductRevenue, ShopRevenue}
import phenix.utils.{ResourceCloseable, SuccessFilter}

class LinearTopRevenueAggregator(dataFileService: DataFileService)
    extends TopRevenueAggregator
        with ResourceCloseable
        with SuccessFilter {

    private val conf = ConfigFactory.load()

    private val groupSize = conf.getInt("transactions.group-size")

    /**
     * Divides the datafile Iterable in entry to get several groups of file that can
     * be Aggregated independently. Then, reduce all the specific results to obtain
     * a the top product of each shop.
     * @param productIds An iterable of product ids
     * @return An iterable of the resulting files
     */
    override def aggregate(productIds: Iterable[Int], date: LocalDate): Iterable[(UUID, ReadableDataFile[ProductRevenue])] = {
        val groups = productIds.grouped(groupSize).zipWithIndex

        val intermediates = groups.map { case (group, groupId) =>
            intermediateAggregation(groupId, group, date)
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
    def intermediateAggregation(groupId: Int, fileGroup: Iterable[Int], date: LocalDate) : Iterable[(UUID, ReadableDataFile[ProductRevenue])] = {
        val initialMap = Map[UUID, List[ProductRevenue]]()

        val aggregations = (initialMap /: fileGroup) {
            case (acc, productId) => tryWith(dataFileService.getShopRevenueReader(productId, date)) { file =>
                val content = filterSuccessValues(file.getContent)
                aggregateProductQuantitiesByShop(content, productId, acc)
            }
        }

        aggregations map { case (shop, productValues) =>
            val sorted = retrieveTop100(productValues)

            tryWith(dataFileService.getIntermediateShopTopRevenueWriter(shop, groupId, date)) {
                _.writeData(sorted)
            }

            (shop, dataFileService.getIntermediateShopTopRevenueReader(shop, groupId, date))
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
                                            productQuantities: Iterable[ShopRevenue],
                                            productId: Int,
                                            map: Map[UUID, List[ProductRevenue]]
                                        ): Map[UUID, List[ProductRevenue]] = {
        (map /: productQuantities) {
            case (acc, productPrice) =>
                val list = acc.getOrElse(productPrice.shop, List())
                acc + (productPrice.shop -> (new ProductRevenue(productId, productPrice.revenue) :: list))
        }
    }

    def reducer(a: Iterable[(UUID, ReadableDataFile[ProductRevenue])], b: Iterable[(UUID, ReadableDataFile[ProductRevenue])]) : Iterable[(UUID, ReadableDataFile[ProductRevenue])] = {

        val date = retrieveDateInFirstFile(a)
        def merged = (a ++ b).groupBy (_._1)

        merged map { case (uuid, files) =>
            val data = files.flatMap {
                case (_, reader) =>
                    tryWith(reader) { _.getContent.toList }
            }
            val processedData = retrieveTop100(filterSuccessValues(data))
            tryWith(dataFileService.getShopTopRevenueWriter(uuid, date)) {
                _.writeData(processedData)
            }
            (uuid, dataFileService.getShopTopRevenueReader(uuid, date))
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

    def retrieveTop100(values: Iterable[ProductRevenue]): Iterable[ProductRevenue] = {
        values.toList.sortBy(- _.price).take(100)
    }


}
