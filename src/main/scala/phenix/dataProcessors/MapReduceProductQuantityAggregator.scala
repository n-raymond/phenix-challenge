package phenix.dataProcessors

import java.time.LocalDate

import phenix.dataFiles.DataFileService
import phenix.models.{ProductQuantity, Transaction}
import phenix.utils.ResourceCloseable
import com.typesafe.config.ConfigFactory
import phenix.dataFiles.general.ReadableDataFile

import scala.util.{Failure, Success, Try}
import scala.collection.immutable.Map

class MapReduceProductQuantityAggregator(dataFileFactory: DataFileService)
    extends ProductQuantityAggregeable with ResourceCloseable {

    private val conf = ConfigFactory.load()

    private val chunkSize = conf.getInt("transactions.chunk-size")



    /** @inheritdoc */
    override def aggregate(transactionFileReader: ReadableDataFile[Transaction]): Iterable[ReadableDataFile[ProductQuantity]] = {
        map(transactionFileReader) map { case (productId, readers) =>
            reducer(productId, readers, transactionFileReader.date)
        }
    }

    /* Map */

    /**
      * Divides the given transaction file into several chunks and aggregate them to produce several IntermediateProductQuantityFile.
      *
      * @param transactionFileReader The transaction file that should be processed to do the aggregation
      * @return A map binding the productId with all the intermediate product quantity files related
      */
    def map(transactionFileReader: ReadableDataFile[Transaction]): Map[Int, Iterable[ReadableDataFile[ProductQuantity]]] = {
        (Map[Int, Stream[ReadableDataFile[ProductQuantity]]]() /: transactionFileReader.getChunks(chunkSize)) {
            case (map, (chunkId, chunk)) =>
                (map /: aggregateChunk(chunk)) {
                    case (innerMap, (productId, productQuantities)) =>
                        tryWith(dataFileFactory.getInterProductQuantityWriter(productId, chunkId, transactionFileReader.date)) {
                            _.writeData(productQuantities)
                        }
                        val freshReader = dataFileFactory.getInterProductQuantityReader(productId, chunkId, transactionFileReader.date)
                            //new IntermediateProductQuantityFile.Reader(productId, chunkId, transactionFileReader.date)
                        innerMap.get(productId) match {
                            case Some(readers) => innerMap + (productId -> freshReader #:: readers)
                            case None => innerMap + (productId -> freshReader #:: Stream[ReadableDataFile[ProductQuantity]]())
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
    def aggregateChunk(chunk: Iterable[Try[Transaction]]): Map[Int, Iterable[ProductQuantity]] = {
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
    def aggregateProductsByShop(transactions: Iterable[Transaction]): Iterable[ProductQuantity] = {
        transactions groupBy {
            _.shop
        } map {
            case (shopId, transactionsOfShop) =>
                val sum = (0 /: transactionsOfShop) {
                    _ + _.quantity
                }
                ProductQuantity(shopId, sum)
        }
    }

    /**
      * Filters the given Try values by keeping only the one who succeeded.
      *
      * @param tries An iterable of the Try values
      * @tparam T The type of the value
      * @return An iterable of the succeeded values.
      */
    def filterSuccessValues[T](tries: Iterable[Try[T]]): Iterable[T] = {
        tries filter {
            case Success(_) => true
            case Failure(_) => false
        } map {
            case Success(transaction) => transaction
            case Failure(e) => throw new IllegalStateException("Can't be a Failure by filter precondition", e)
        }
    }


    /* Reducer */

    /**
      * Takes all the intermediate product quantity file readers associated to a productId and aggregate their
      * data to produce a product quantity file. Finally, returns a reader of the resulting file.
      * @param productId The id of the product
      * @param readers Intermediate quantity file associated with the product id
      * @param date The date of the sold of the product
      * @return A reader to file containing the aggregation of all the intermediate files.
      */
    def reducer(productId: Int, readers : Iterable[ReadableDataFile[ProductQuantity]], date: LocalDate) = {

        val lines = tryWith(readers) {
            _ flatMap { reader =>
                filterSuccessValues(reader.getContent)
            } groupBy {
                _.shop
            } map {
                case (shopId, quantities) =>
                    ProductQuantity(shopId, (0 /: quantities)  (_ + _.quantity))
            }
        }

        tryWith(dataFileFactory.getProductQuantityWriter(productId, date)) {
            _.writeData(lines)
        }

        dataFileFactory.getProductQuantityReader(productId, date)
    }

}