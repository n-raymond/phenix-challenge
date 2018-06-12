package phenix.dataFiles.specifics

import java.time.LocalDate

import phenix.dataFiles.general.{ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ProductQuantity


/**
  * A helper to manage intermediates files containing quantity of a product.
  * Those files will be used as a pre-computing of a product quantity file.
  *
  * @param productId The id of the product
  * @param date      The date of the DatedDataFile
  */
class IntermediateProductQuantityFile(productId: Int, val groupId: Int, date: LocalDate, ioService: IOService)
    extends ProductQuantityFile(productId, date, ioService) {

    /** @inheritdoc*/
    override def fileNamePrefix: String = s"intermediate_product_qty_${productId}_$groupId"

    /** @inheritdoc*/
    override def fileLocation: String = s"${super.fileLocation}/inter"

}

object IntermediateProductQuantityFile {

    class Reader(productId: Int, groupId: Int, date: LocalDate, ioService: IOService)
        extends IntermediateProductQuantityFile(productId, groupId, date, ioService)
            with ReadableDataFileImpl[ProductQuantity]


    class Writer(productId: Int, groupId: Int, date: LocalDate, ioService: IOService)
        extends IntermediateProductQuantityFile(productId, groupId, date, ioService)
            with WritableDataFileImpl[ProductQuantity]

}


