package phenix.dataProcessors.shopRevenueAggregation

import java.time.LocalDate

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.ShopRevenue

/**
  * Offers a way to extract each product revenue.
  */
trait ShopRevenueAggregator {

    def aggregate(productQuantities: Iterable[Int], date: LocalDate): Iterable[(Int, ReadableDataFile[ShopRevenue])]

}
