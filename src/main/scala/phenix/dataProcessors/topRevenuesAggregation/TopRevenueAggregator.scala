package phenix.dataProcessors.topRevenuesAggregation

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.ProductRevenue


/**
  * Offers a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait TopRevenueAggregator {

    def aggregate(transactionFileReader: Iterable[Int], date: LocalDate) : Iterable[(UUID, ReadableDataFile[ProductRevenue])]

}
