package phenix.dataFiles.specifics

import java.time.LocalDate

import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.io.IOService
import phenix.models.ShopQuantity


/**
  * A helper to manage files containing the quantity of a product.
  *
  * @param productId The id of the product
  * @param date The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class ShopQuantityFile(val productId: Int, date: LocalDate, ioService: IOService)
    extends DataFileImpl[ShopQuantity](date, ioService)
        with LocatedInResult[ShopQuantity] {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"product_qty_$productId"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): phenix.models.ShopQuantity = ShopQuantity(serializedData)

    /** @inheritdoc */
    override def serializeData(data: ShopQuantity): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/product_quantity"
}


object ShopQuantityFile {

    class Reader(productId: Int, date: LocalDate, ioService: IOService)
        extends ShopQuantityFile(productId, date, ioService)
            with ReadableDataFileImpl[ShopQuantity]

    class Writer(productId: Int, date: LocalDate, ioService: IOService)
        extends ShopQuantityFile(productId, date, ioService)
            with WritableDataFileImpl[ShopQuantity]

}
