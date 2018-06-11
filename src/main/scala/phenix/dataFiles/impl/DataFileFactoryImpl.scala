package phenix.dataFiles.impl

import java.time.LocalDate

import phenix.dataFiles._
import phenix.dataFiles.impl.specifics.{IntermediateProductQuantityFile, ProductQuantityFile}
import phenix.models.ProductQuantity

class DataFileFactoryImpl extends DataFileFactory {

    /** @inheritdoc */
    override def getProductQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ProductQuantity] = {
        new ProductQuantityFile.Reader(productId, date)
    }

    /** @inheritdoc */
    override def getProductQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ProductQuantity] = {
        new ProductQuantityFile.Writer(productId, date)
    }

    /** @inheritdoc */
    override def getInterProductQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ProductQuantity] = {
        new IntermediateProductQuantityFile.Reader(productId, groupId, date)
    }

    /** @inheritdoc */
    override def getInterProductQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ProductQuantity] = {
        new IntermediateProductQuantityFile.Writer(productId, groupId, date)
    }
}
