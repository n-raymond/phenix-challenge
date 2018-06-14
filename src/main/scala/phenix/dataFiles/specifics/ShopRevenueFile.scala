package phenix.dataFiles.specifics

import java.time.LocalDate

import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ShopRevenue

/**
  * A helper to manage file used to bind shops to revenues for a given product
  * at a certain day.
  * @param productId The id of the product
  * @param date The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class ShopRevenueFile(val productId: Int, date: LocalDate, ioService: IOService)
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


object ShopRevenueFile {

    class Reader(productId: Int, date: LocalDate, ioService: IOService)
        extends ShopRevenueFile(productId, date, ioService)
            with ReadableDataFileImpl[ShopRevenue]

    class Writer(productId: Int, date: LocalDate, ioService: IOService)
        extends ShopRevenueFile(productId, date, ioService)
            with WritableDataFileImpl[ShopRevenue]

}
