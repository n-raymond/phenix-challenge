package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ProductQuantity
import phenix.utils.DateSerializer


/**
  * A helper to manage files files of shop top sells .
  *
  * @param shop The id of the shop
  * @param date The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class ShopTopQuantityFile(val shop: UUID, date: LocalDate, ioService: IOService)
    extends DataFileImpl[ProductQuantity](date, ioService)
        with LocatedInResult[ProductQuantity]
        with DateSerializer {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"top_100_ventes_$shop"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): ProductQuantity = ProductQuantity(serializedData)

    /** @inheritdoc */
    override def serializeData(data: ProductQuantity): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/top_100_ventes"
}


object ShopTopQuantityFile {

    class Reader(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopQuantityFile(shop, date, ioService)
            with ReadableDataFileImpl[ProductQuantity]


    class Writer(shop: UUID, date: LocalDate, ioService: IOService)
        extends ShopTopQuantityFile(shop, date, ioService)
            with WritableDataFileImpl[ProductQuantity]

}