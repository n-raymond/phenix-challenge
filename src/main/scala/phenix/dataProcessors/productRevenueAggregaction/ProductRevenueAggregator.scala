package phenix.dataProcessors.productRevenueAggregaction

import phenix.dataFiles.general.ReadableDataFile
import phenix.models.{ProductPrice, ShopQuantity, ShopRevenue}

/**
  * Offers a way to extract each product revenue
  * day in each shop.
  */
trait ProductRevenueAggregator {

    def aggregate(productQuantities: Iterable[(Int, ReadableDataFile[ShopQuantity])]): Iterable[(Int, ReadableDataFile[ShopRevenue])]

}
