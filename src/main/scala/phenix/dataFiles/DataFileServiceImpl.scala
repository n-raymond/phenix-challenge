package phenix.dataFiles

import java.time.LocalDate

import phenix.dataFiles.general.{ReadableDataFile, WritableDataFile}
import phenix.dataFiles.specifics.{IntermediateProductQuantityFile, ProductQuantityFile}
import phenix.io.IOService
import phenix.models.ProductQuantity

class DataFileServiceImpl(ioService: IOService) extends DataFileService {

    /** @inheritdoc */
    override def getProductQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ProductQuantity] = {
        new ProductQuantityFile.Reader(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getProductQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ProductQuantity] = {
        new ProductQuantityFile.Writer(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getInterProductQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ProductQuantity] = {
        new IntermediateProductQuantityFile.Reader(productId, groupId, date, ioService)
    }

    /** @inheritdoc */
    override def getInterProductQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ProductQuantity] = {
        new IntermediateProductQuantityFile.Writer(productId, groupId, date, ioService)
    }

}
