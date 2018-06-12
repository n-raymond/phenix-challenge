package phenix.dataFiles

import java.time.LocalDate

import phenix.dataFiles.general.{ReadableDataFile, WritableDataFile}
import phenix.dataFiles.specifics.{IntermediateProductQuantityFile, ProductQuantityFile, TransactionFileReader}
import phenix.io.IOService
import phenix.models.{ProductQuantity, Transaction}

class DataFileServiceImpl(ioService: IOService) extends DataFileService {

    /** @inheritdoc */
    override def getTransactionReader(date: LocalDate): ReadableDataFile[Transaction] = {
        new TransactionFileReader(date, ioService)
    }

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
