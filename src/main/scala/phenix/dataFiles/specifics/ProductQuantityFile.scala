package phenix.dataFiles.specifics

import java.time.LocalDate

import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.models.ProductQuantity


/**
  * A helper to manage files quantity of a product.
  *
  * @param productId The id of the product
  * @param date The date of the DatedDataFile
  */
class ProductQuantityFile(val productId: Int, date: LocalDate)
    extends DataFileImpl[ProductQuantity](date)
        with LocatedInResult[ProductQuantity] {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"product_qty_$productId"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): phenix.models.ProductQuantity = ProductQuantity(serializedData)

    /** @inheritdoc */
    override def serializeData(data: ProductQuantity): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/product_quantity"
}


object ProductQuantityFile {

    class Reader(productId: Int, date: LocalDate)
        extends ProductQuantityFile(productId, date)
            with ReadableDataFileImpl[ProductQuantity]

    class Writer(productId: Int, date: LocalDate)
        extends ProductQuantityFile(productId, date)
            with WritableDataFileImpl[ProductQuantity]

}
