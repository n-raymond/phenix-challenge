package phenix.dataFiles

import java.time.LocalDate


/**
  * A helper to manage intermediates files quantity of a product.
  * Those files will be used as a pre-computing of a product quantity file.
  *
  * @param productId The id of the product
  * @param date      The date of the DatedDataFile
  */
class IntermediateProductQuantityFile(productId: Int, val groupId: Int, date: LocalDate)
    extends ProductQuantityFile(productId, date) {

    /** @inheritdoc*/
    override def fileNamePrefix: String = s"intermediate_product_qty_${productId}_$groupId"

    /** @inheritdoc*/
    override def fileLocation: String = s"${super.fileLocation}/inter"

}

object IntermediateProductQuantityFile {

    class Reader(productId: Int, groupId: Int, date: LocalDate)
        extends IntermediateProductQuantityFile(productId, groupId, date)
            with DatedDataFile.Readable


    class Writer(productId: Int, groupId: Int, date: LocalDate)
        extends IntermediateProductQuantityFile(productId, groupId, date)
            with DatedDataFile.Writable

}


