package phenix.dataFiles.specifics

import java.time.LocalDate

import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ShopRevenue

/**
  * A helper to manage file containing a product revenue for each
  * shop at a certain day.
  *
  * @param productId The id of the product
  * @param date The date of the DatedDataFile
  */
class ProductRevenueFile(val productId: Int, date: LocalDate, ioService: IOService)
    extends DataFileImpl[ShopRevenue](date, ioService)
        with LocatedInResult[ShopRevenue] {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"product_revenue_$productId"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): phenix.models.ShopRevenue = ShopRevenue(serializedData)

    /** @inheritdoc */
    override def serializeData(data: ShopRevenue): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/product_revenue"
}


object ProductRevenueFile {

    class Reader(productId: Int, date: LocalDate, ioService: IOService)
        extends ProductRevenueFile(productId, date, ioService)
            with ReadableDataFileImpl[ShopRevenue]

    class Writer(productId: Int, date: LocalDate, ioService: IOService)
        extends ProductRevenueFile(productId, date, ioService)
            with WritableDataFileImpl[ShopRevenue]

}
