package phenix.dataFiles.specifics

import java.time.LocalDate

import phenix.dataFiles.general.{ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ShopQuantity


/**
  * A helper to manage intermediates files containing quantity of a product.
  * Those files will be used as a pre-computing of a product quantity file.
  *
  * @param productId The id of the product
  * @param date      The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class IntermediateShopQuantityFile(productId: Int, val groupId: Int, date: LocalDate, ioService: IOService)
    extends ShopQuantityFile(productId, date, ioService) {

    /** @inheritdoc*/
    override def fileNamePrefix: String = s"intermediate_product_qty_${productId}_$groupId"

    /** @inheritdoc*/
    override def fileLocation: String = s"${super.fileLocation}/inter"

}

object IntermediateShopQuantityFile {

    class Reader(productId: Int, groupId: Int, date: LocalDate, ioService: IOService)
        extends IntermediateShopQuantityFile(productId, groupId, date, ioService)
            with ReadableDataFileImpl[ShopQuantity]


    class Writer(productId: Int, groupId: Int, date: LocalDate, ioService: IOService)
        extends IntermediateShopQuantityFile(productId, groupId, date, ioService)
            with WritableDataFileImpl[ShopQuantity]

}


