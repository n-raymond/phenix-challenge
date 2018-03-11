package phenix.dataProcessors

import phenix.dataFiles.{IntermediateProductQuantityFile, ProductQuantityFile, TransactionFileReader}
import phenix.models.{ProductQuantity, Transaction}
import phenix.utils.ResourceClosable

import scala.util.{Failure, Success, Try}


object MapReduceQuantityAggregator extends ProductQuantityAggregable with ResourceClosable {

    override def aggregate(transactionFileReader: TransactionFileReader): Iterable[ProductQuantityFile.Reader] = {

        map(transactionFileReader)
        ???

    }

    def map(transactionFileReader: TransactionFileReader, chunkSize: Int = 1024) : (Set[Int], Set[Int]) = {

        transactionFileReader.getChunks(chunkSize) foreach { chunk =>
            aggregateChunk(chunk._2)
        }
        ???

    }

    /**
      *
      * @param chunk
      * @return
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
      * @param transactions A Stream containing
      * @return Several ProductQuantities resulting from the aggregation
      */
    def aggregateProductsByShop(transactions: Iterable[Transaction]): Iterable[ProductQuantity] = {
        transactions groupBy {
            _.shop
        } map {
            case (shopId, transactionsOfShop) =>
                val sum = (0 /: transactionsOfShop) {_ + _.quantity}
                ProductQuantity(shopId, sum)
        }
    }

    /**
      * Filters the given Try values by keeping only the one who succeeded.
      * @param tries An iterable of the Try values
      * @tparam T The type of the value
      * @return An iterable of the succeeded values.
      */
    def filterSuccessValues[T](tries: Iterable[Try[T]]) : Iterable[T] = {
        tries filter {
            case Success (_) => true
            case Failure (_) => false
        } map {
            case Success (transaction) => transaction
            case Failure (e) => throw new IllegalStateException ("Can't be a Failure by filter precondition", e)
        }
    }

}
