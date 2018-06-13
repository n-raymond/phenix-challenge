package phenix.dataProcessors.topSalesAggregation


import java.util.UUID

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ProductQuantity, ProductValue}


/**
  * Offers a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait TopSalesAggregator {

    def aggregate(transactionFileReader: Iterable[(Int, ReadableDataFile[ProductQuantity])]) : Iterable[(UUID, ReadableDataFile[ProductValue])]

}
