package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ProductValue
import phenix.utils.DateSerializer


/**
  * A helper to manage files shop to sells files.
  *
  * @param shop The id of the shop
  * @param date The date of the DatedDataFile
  */
class ShopTopSellsFile(val shop: UUID, date: LocalDate, ioService: IOService)
    extends DataFileImpl[ProductValue](date, ioService)
        with LocatedInResult[ProductValue]
        with DateSerializer {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"top_100_ventes_$shop"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): ProductValue = ProductValue(serializedData)

    /** @inheritdoc */
    override def serializeData(data: ProductValue): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/top_100_ventes"
}


object ShopTopSellsFile {

    class Reader(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopSellsFile(shop, date, ioService)
            with ReadableDataFileImpl[ProductValue]


    class Writer(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopSellsFile(shop, date, ioService)
            with WritableDataFileImpl[ProductValue]

}