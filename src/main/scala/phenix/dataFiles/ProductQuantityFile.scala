package phenix.dataFiles

import java.time.LocalDate

import phenix.models.ProductQuantity


/**
  * A helper to manage files quantity of a product.
  *
  * @param productId The id of the procut
  * @param date The date of the DatedDataFile
  */
abstract class ProductQuantityFile(val productId: Int, date: LocalDate) extends DatedDataFile(date)  {

    /** @inheritdoc */
    override type Data = ProductQuantity

    /** @inheritdoc */
    override protected def fileNamePrefix: String = s"product_qty_${productId}"

    /** @inheritdoc */
    override protected def parseData(serializedData: String): ProductQuantity = ProductQuantity(serializedData)

}


object ProductQuantityFile {

    class Reader(productId: Int, date: LocalDate)
        extends ProductQuantityFile(productId, date)
            with DatedDataFile.Readable


    class Writer(productId: Int, date: LocalDate)
        extends ProductQuantityFile(productId, date)
            with DatedDataFile.Writable

}
