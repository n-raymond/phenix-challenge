package phenix.dataProcessors

import phenix.dataFiles.ReadableDataFile
import phenix.models.{ProductQuantity, Transaction}


/**
  * A ProductQuantityAggregator offer a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait ProductQuantityAggregeable {

    def aggregate(transactionFileReader: ReadableDataFile[Transaction]) : Iterable[ReadableDataFile[ProductQuantity]]

}
