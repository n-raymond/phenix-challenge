package phenix.dataProcessors.productRevenueAggregaction

import java.time.LocalDate

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.ShopRevenue

/**
  * Offers a way to extract each product revenue
  * day in each shop.
  */
trait ProductRevenueAggregator {

    def aggregate(productQuantities: Iterable[Int], date: LocalDate): Iterable[(Int, ReadableDataFile[ShopRevenue])]

}
