package phenix.dataProcessors.topValuesAggregation


import java.util.UUID

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ShopQuantity, ProductValue}


/**
  * Offers a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait TopValuesAggregator {

    def aggregate(transactionFileReader: Iterable[(Int, ReadableDataFile[ShopQuantity])]) : Iterable[(UUID, ReadableDataFile[ProductValue])]

}
