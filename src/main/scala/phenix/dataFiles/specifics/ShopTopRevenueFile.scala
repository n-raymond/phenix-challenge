package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.io.IOService
import phenix.models.ProductPrice
import phenix.utils.DateSerializer


/**
  * A helper to manage files shop to sells files.
  *
  * @param shop The id of the shop
  * @param date The date of the DatedDataFile
  */
class ShopTopRevenueFile(val shop: UUID, date: LocalDate, ioService: IOService)
    extends DataFileImpl[ProductPrice](date, ioService)
        with LocatedInResult[ProductPrice]
        with DateSerializer {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"top_100_ca_$shop"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): ProductPrice = ProductPrice(serializedData)

    /** @inheritdoc */
    override def serializeData(data: ProductPrice): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/top_100_ca"
}

object ShopTopRevenueFile {

    class Reader(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopRevenueFile(shop, date, ioService)
            with ReadableDataFileImpl[ProductPrice]


    class Writer(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopRevenueFile(shop, date, ioService)
            with WritableDataFileImpl[ProductPrice]

}
