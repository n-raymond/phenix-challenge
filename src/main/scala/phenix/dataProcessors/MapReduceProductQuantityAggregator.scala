package phenix.dataProcessors

import phenix.dataFiles.{IntermediateProductQuantityFile, ProductQuantityFile, TransactionFileReader}
import phenix.models.{ProductQuantity, Transaction}
import phenix.utils.ResourceCloseable

import com.typesafe.config.ConfigFactory

import scala.util.{Failure, Success, Try}
import scala.collection.immutable.Map

object MapReduceProductQuantityAggregator extends ProductQuantityAggregeable with ResourceCloseable {

    private val conf = ConfigFactory.load()

    private val chunkSize = conf.getInt("transactions.chunk-size")

    override def aggregate(transactionFileReader: TransactionFileReader): Iterable[ProductQuantityFile.Reader] = {

        map(transactionFileReader)
        ???

    }

    /* Map */

    /**
      * Divides the given transaction file into several chunks and aggregate them to produce several IntermediateProductQuantityFile.
      *
      * @param transactionFileReader The transaction file that should be processed to do the aggregation
      * @return A map binding the productId with all the intermediate product quantity files related
      */
    def map(transactionFileReader: TransactionFileReader): Map[Int, Iterable[IntermediateProductQuantityFile.Reader]] = {
        (Map[Int, Stream[IntermediateProductQuantityFile.Reader]]() /: transactionFileReader.getChunks(chunkSize)) {
            case (map, (chunkId, chunk)) =>
                (map /: aggregateChunk(chunk)) {
                    case (innerMap, (productId, productQuantities)) =>
                        tryWith(new IntermediateProductQuantityFile.Writer(productId, chunkId, transactionFileReader.date)) {
                            _.writeData(productQuantities)
                        }
                        val freshReader = new IntermediateProductQuantityFile.Reader(productId, chunkId, transactionFileReader.date)
                        innerMap.get(productId) match {
                            case Some(readers) => map + (productId -> freshReader #:: readers)
                            case None => map + (productId -> freshReader #:: Stream[IntermediateProductQuantityFile.Reader]())
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


    /* Reduce */




}