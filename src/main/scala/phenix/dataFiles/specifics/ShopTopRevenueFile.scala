package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.io.IOService
import phenix.models.ProductRevenue
import phenix.utils.DateSerializer


/**
  * A helper to manage files shop top revenue files.
  *
  * @param shop The id of the shop
  * @param date The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class ShopTopRevenueFile(val shop: UUID, date: LocalDate, ioService: IOService)
    extends DataFileImpl[ProductRevenue](date, ioService)
        with LocatedInResult[ProductRevenue]
        with DateSerializer {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"top_100_ca_$shop"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): ProductRevenue = ProductRevenue(serializedData)

    /** @inheritdoc */
    override def serializeData(data: ProductRevenue): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/top_100_ca"
}

object ShopTopRevenueFile {

    class Reader(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopRevenueFile(shop, date, ioService)
            with ReadableDataFileImpl[ProductRevenue]


    class Writer(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopRevenueFile(shop, date, ioService)
            with WritableDataFileImpl[ProductRevenue]

}
