package phenix.dataProcessors.productQuantityAggregation

import java.time.LocalDate

import com.typesafe.config.ConfigFactory
import phenix.dataFiles.DataFileService
import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ShopQuantity, Transaction}
import phenix.utils.{ResourceCloseable, SuccessFilter}

import scala.collection.immutable
import scala.collection.immutable.Map
import scala.util.{Failure, Success, Try}

class LinearProductQuantityAggregator(dataFileFactory: DataFileService)
    extends ProductQuantityAggregator
        with ResourceCloseable
        with SuccessFilter {

    private val conf = ConfigFactory.load()

    private val chunkSize = conf.getInt("transactions.chunk-size")



    /** @inheritdoc */
    override def aggregate(transactionFileReader: ReadableDataFile[Transaction]): Iterable[Int] = {
        tryWith (transactionFileReader) {
            map(_) map { case (productId, readers) =>
                reducer(productId, readers, transactionFileReader.date)
                productId
            }
        }
    }

    /* Map */

    /**
      * Divides the given transaction file into several chunks and aggregate them to produce several IntermediateProductQuantityFile.
      *
      * @param transactionFileReader The transaction file that should be processed to do the aggregation
      * @return A map binding the productId with all the intermediate product quantity files related
      */
    def map(transactionFileReader: ReadableDataFile[Transaction]): Map[Int, Iterable[ReadableDataFile[ShopQuantity]]] = {

        val chunks = transactionFileReader.getChunks(chunkSize)
        val initialMap = Map[Int, Stream[ReadableDataFile[ShopQuantity]]]()

        (initialMap /: chunks) {
            case (map, (chunkId, chunk)) =>
                (map /: aggregateChunk(chunk)) {
                    case (innerMap, (productId, productQuantities)) =>
                        tryWith(dataFileFactory.getInterProductQuantityWriter(productId, chunkId, transactionFileReader.date)) {
                            _.writeData(productQuantities)
                        }
                        val freshReader = dataFileFactory.getInterProductQuantityReader(productId, chunkId, transactionFileReader.date)

                        innerMap.get(productId) match {
                            case Some(readers) => innerMap + (productId -> freshReader #:: readers)
                            case None => innerMap + (productId -> freshReader #:: Stream[ReadableDataFile[ShopQuantity]]())
                        }
                }
        }
    }

    /**
      * Aggregates each tried transaction by grouping them by product and summing the quantity of
      * the one which came from the same shop.
      *
      * @param chunk A chunk containing some tried transactions
      * @return A map that binds the productId to an iterable of ProductQuantity
      */
    def aggregateChunk(chunk: Iterable[Try[Transaction]]): Map[Int, Iterable[ShopQuantity]] = {
        filterSuccessValues(chunk) groupBy {
            _.product
        } map {
            case (productId, transactionsOfProduct) => (productId, aggregateProductsByShop(transactionsOfProduct))
        }
    }

    /**
      * Aggregates each given Transaction that came from the same shop and
      * add their quantity to produce some ProductQuantities.
      *
      * @param transactions A Stream containing
      * @return Several ProductQuantities resulting from the aggregation
      */
    def aggregateProductsByShop(transactions: Iterable[Transaction]): Iterable[ShopQuantity] = {
        transactions groupBy {
            _.shop
        } map {
            case (shopId, transactionsOfShop) =>
                val sum = (0 /: transactionsOfShop) {
                    _ + _.quantity
                }
                ShopQuantity(shopId, sum)
        }
    }


    /* Reducer */

    /**
      * Takes all the intermediate product quantity file readers associated to a productId and aggregate their
      * data to produce a product quantity file.
      * @param productId The id of the product
      * @param readers Intermediate quantity file associated with the product id
      * @param date The date of the sold of the product
      * @return A reader to file containing the aggregation of all the intermediate files.
      */
    def reducer(productId: Int, readers : Iterable[ReadableDataFile[ShopQuantity]], date: LocalDate) = {

        val lines = tryWith(readers) {
            _ flatMap { reader =>
                filterSuccessValues(reader.getContent)
            } groupBy {
                _.shop
            } map {
                case (shopId, quantities) =>
                    ShopQuantity(shopId, (0 /: quantities)  (_ + _.quantity))
            }
        }

        tryWith(dataFileFactory.getProductQuantityWriter(productId, date)) {
            _.writeData(lines)
        }
    }

}