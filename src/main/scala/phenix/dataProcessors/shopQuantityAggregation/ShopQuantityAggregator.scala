package phenix.dataProcessors.shopQuantityAggregation

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ShopQuantity, Transaction}


/**
  * Offers a way to process transactions
  * and aggregate the sum of each sold product.
  */
trait ShopQuantityAggregator {

    /**
      *
      * @param transactionFileReader
      * @return
      */
    def aggregate(transactionFileReader: ReadableDataFile[Transaction]) : Iterable[Int]

}
