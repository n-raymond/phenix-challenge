package phenix.dataProcessors.productQuantityAggregation

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ProductQuantity, Transaction}


/**
  * Offers a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait ProductQuantityAggregator {

    /**
      *
      * @param transactionFileReader
      * @return
      */
    def aggregate(transactionFileReader: ReadableDataFile[Transaction]) : Iterable[(Int, ReadableDataFile[ProductQuantity])]

}
