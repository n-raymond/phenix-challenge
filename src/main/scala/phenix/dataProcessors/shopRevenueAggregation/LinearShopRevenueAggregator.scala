package phenix.dataProcessors.shopRevenueAggregation
import java.io.IOException
import java.time.LocalDate
import java.util.UUID

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import phenix.dataFiles.DataFileService
import phenix.dataFiles.general.ReadableDataFile
import phenix.dataFiles.specifics.ReferenceFile
import phenix.io.IOService
import phenix.models.{ShopQuantity, ShopRevenue}
import phenix.utils.{ResourceCloseable, SuccessFilter}

class LinearShopRevenueAggregator(dataFileService: DataFileService, ioService: IOService)
    extends ShopRevenueAggregator
        with ResourceCloseable
        with SuccessFilter
        with LazyLogging {

    /**
      * The application configuration
      */
    private val conf = ConfigFactory.load()

    override def aggregate(productIds: Iterable[Int], date: LocalDate): Iterable[(Int, ReadableDataFile[ShopRevenue])] = {
        createProductPriceFiles()

        productIds map { productId =>
            tryWith(dataFileService.getShopQuantityReader(productId, date)) { reader =>
                val content = filterSuccessValues(reader.getContent)
                val revenues = computeRevenues(content, productId, date)

                tryWith(dataFileService.getShopRevenueWriter(productId, date)) {
                    _.writeData(revenues)
                } {
                    logger.error(s"Can not write on ShopRevenue file with id: $productId", _)
                }

                (productId, dataFileService.getShopRevenueReader(productId, date))
            } { e =>
                logger.error(s"Can not read a ShopQuantity file with id: $productId", e)
                throw e
            }
        }
    }

    /**
      * Computes for each product the revenues of a shop.
      * It will use the bindings of (shop, quantity) that was already computed to multiply the quantity
      * with the price of the product.
      * @param shopQuantities The files containing bindings of (shop, quantity) for a product
      * @param productId The product in question
      * @param date The date of the transactions
      * @return An iterable containing the resulting shop revenues for the product
      */
    def computeRevenues(shopQuantities: Iterable[ShopQuantity], productId: Int, date: LocalDate): Iterable[ShopRevenue] = {
        shopQuantities map { case ShopQuantity(shop, qty) =>
            try {
                val price = getProductPrice(shop, productId, date)
                new ShopRevenue(shop, price * qty)
            } catch {
                case _: IOException => new ShopRevenue(shop, 0.0)
            }
        }
    }

    /**
      * Retrieve the price of a certain product in a given shop on a given date.
      * @param shop The shop in which the product was sold
      * @param productId The product that was sold
      * @param date The date of the sold
      * @return The price of this product
      */
    def getProductPrice(shop: UUID, productId: Int, date: LocalDate): Double = {
        tryWith(dataFileService.getPriceReader(shop, productId, date)) { reader =>
            filterSuccessValues(reader.getContent).head
        } { e =>
            logger.error(s"Can not write a price file", e)
            throw e
        }
    }

    /**
      * Reads all the shop reference files and explodes them to store the price
      * of each product for each shop in a single file.
      */
    def createProductPriceFiles()= {
        val dataDirectoryName = conf.getString("paths.data")
        val referenceNames = ioService.findFilesNameByRegex(dataDirectoryName, ReferenceFile.fileNameRegex)

        referenceNames foreach { name =>
            val shop = ReferenceFile.getUUIDFromFileName(name)
            val date = ReferenceFile.getDateFromFilename(name)
            val content = dataFileService.getReferenceReader(shop, date).getContent.toList

            tryWith(dataFileService.getReferenceReader(shop, date)) { reader =>
                val content = filterSuccessValues(reader.getContent)
                content foreach { productValue =>

                    tryWith(dataFileService.getPriceWriter(shop, productValue.product, date)) {
                        _.writeData(Iterable(productValue.price))
                    } {
                        logger.error(s"Can not write a price file with id: ${productValue.product} with shop: $shop", _)
                    }
                }
            } { e =>
                logger.error(s"Critical : Can not read a reference file", e)
                throw e
            }
        }
    }

}
