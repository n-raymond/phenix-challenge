package phenix.dataFiles

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.{ReadableDataFile, WritableDataFile}
import phenix.dataFiles.specifics._
import phenix.io.IOService
import phenix.models.{ProductQuantity, ProductValue, Transaction}

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

    /** @inheritdoc */
    override def getShopTopSellsReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductValue] = {
        new ShopTopSellsFile.Reader(shop, date, ioService)
    }

    /** @inheritdoc */
    override def getShopTopSellsWriter(shop: UUID, date: LocalDate): WritableDataFile[ProductValue] = {
        new ShopTopSellsFile.Writer(shop, date, ioService)
    }

    /** @inheritdoc */
    override def getIntermediateShopTopSellsReader(shop: UUID, group: Int, date: LocalDate): ReadableDataFile[ProductValue] = {
        new IntermediateShopToSellsFile.Reader(shop, group, date, ioService)
    }

    /** @inheritdoc */
    override def getIntermediateShopTopSellsWriter(shop: UUID, group: Int, date: LocalDate): WritableDataFile[ProductValue] = {
        new IntermediateShopToSellsFile.Writer(shop, group, date, ioService)
    }
}
