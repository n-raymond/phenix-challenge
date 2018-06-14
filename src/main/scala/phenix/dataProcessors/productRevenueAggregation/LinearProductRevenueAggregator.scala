package phenix.dataProcessors.productRevenueAggregation
import java.time.LocalDate
import java.util.UUID

import com.typesafe.config.ConfigFactory
import phenix.dataFiles.DataFileService
import phenix.dataFiles.general.{DataFile, ReadableDataFile}
import phenix.dataFiles.specifics.ReferenceFile
import phenix.io.path.FileFinder
import phenix.models.{ShopQuantity, ShopRevenue}
import phenix.utils.{ResourceCloseable, SuccessFilter}

class LinearProductRevenueAggregator(dataFileService: DataFileService)
    extends ProductRevenueAggregator
        with FileFinder
        with ResourceCloseable
        with SuccessFilter {

    /**
      * The application configuration
      */
    private val conf = ConfigFactory.load()

    /*override def aggregate(productQuantities: Iterable[(Int, ReadableDataFile[ShopQuantity])]):
        Iterable[(Int, ReadableDataFile[ShopRevenue])] = {

        val date = retrieveDateInFirstFile(productQuantities)

        createProductPriceFiles()

        productQuantities map { case (productId, reader) =>
            tryWith(reader) { reader =>
                val content = filterSuccessValues(reader.getContent)
                val revenues = computeRevenues(content, productId, date)
                tryWith(dataFileService.getProductRevenueWriter(productId, date)) {
                    _.writeData(revenues)
                }
                (productId, dataFileService.getProductRevenueReader(productId, date))
            }
        }
    }*/

    override def aggregate(productIds: Iterable[Int], date: LocalDate): Iterable[(Int, ReadableDataFile[ShopRevenue])] = {
        createProductPriceFiles()

        productIds map { productId =>
            tryWith(dataFileService.getProductQuantityReader(productId, date)) { reader =>
                val content = filterSuccessValues(reader.getContent)
                val revenues = computeRevenues(content, productId, date)
                tryWith(dataFileService.getProductRevenueWriter(productId, date)) {
                    _.writeData(revenues)
                }
                (productId, dataFileService.getProductRevenueReader(productId, date))
            }
        }
    }

    def computeRevenues(productQties: Iterable[ShopQuantity], productId: Int, date: LocalDate): Iterable[ShopRevenue] = {
        productQties map { case ShopQuantity(shop, qty) =>
            val price = getProductPrice(shop, productId, date)
            new ShopRevenue(shop, price * qty)
        }
    }

    def getProductPrice(shop: UUID, productId: Int, date: LocalDate): Double = {
        tryWith(dataFileService.getProductPriceReader(shop, productId, date)) { reader =>
            filterSuccessValues(reader.getContent).head
        }
    }

    def createProductPriceFiles()= {
        val dataDirectoryName = conf.getString("paths.data")
        val referenceNames = findFilesNameByRegex(dataDirectoryName, ReferenceFile.fileNameRegex)

        referenceNames foreach { name =>
            val shop = ReferenceFile.getUUIDFromFileName(name)
            val date = ReferenceFile.getDateFromFilename(name)
            val content = dataFileService.getReferenceReader(shop, date).getContent.toList

            tryWith(dataFileService.getReferenceReader(shop, date)) { reader =>
                val content = filterSuccessValues(reader.getContent)
                content foreach { productValue =>
                    tryWith(dataFileService.getProductPriceWriter(shop, productValue.product, date)) {
                        _.writeData(Iterable(productValue.value))
                    }
                }
            }
        }
    }

}
