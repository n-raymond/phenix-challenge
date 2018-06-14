package phenix.dataProcessors.topValuesAggregation


import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ProductValue, ShopQuantity}


/**
  * Offers a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait TopValuesAggregator {

    def aggregate(transactionFileReader: Iterable[Int], date: LocalDate) : Iterable[(UUID, ReadableDataFile[ProductValue])]

}
