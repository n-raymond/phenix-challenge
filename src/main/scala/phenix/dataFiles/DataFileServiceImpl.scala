package phenix.dataFiles

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.{ReadableDataFile, WritableDataFile}
import phenix.dataFiles.specifics._
import phenix.io.IOService
import phenix.models._

class DataFileServiceImpl(ioService: IOService) extends DataFileService {

    /** @inheritdoc */
    override def getTransactionReader(date: LocalDate): ReadableDataFile[Transaction] = {
        new TransactionFileReader(date, ioService)
    }

    /** @inheritdoc */
    override def getReferenceReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductPrice] = {
        new ReferenceFile(shop, date, ioService)
    }

    /** @inheritdoc */
    override def getProductQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ShopQuantity] = {
        new ProductQuantityFile.Reader(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getProductQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ShopQuantity] = {
        new ProductQuantityFile.Writer(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getInterProductQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ShopQuantity] = {
        new IntermediateProductQuantityFile.Reader(productId, groupId, date, ioService)
    }

    /** @inheritdoc */
    override def getInterProductQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ShopQuantity] = {
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
        new IntermediateShopTopSellsFile.Reader(shop, group, date, ioService)
    }

    /** @inheritdoc */
    override def getIntermediateShopTopSellsWriter(shop: UUID, group: Int, date: LocalDate): WritableDataFile[ProductValue] = {
        new IntermediateShopTopSellsFile.Writer(shop, group, date, ioService)
    }

    /** @inheritdoc */

    override def getProductPriceReader(shop: UUID, productId: Int, date: LocalDate): ReadableDataFile[Double] = {
        new ProductPriceFile.Reader(shop, productId, date, ioService)
    }

    /** @inheritdoc */
    override def getProductPriceWriter(shop: UUID, productId: Int, date: LocalDate): WritableDataFile[Double] = {
        new ProductPriceFile.Writer(shop, productId, date, ioService)
    }

    /** @inheritdoc */
    override def getProductRevenueReader(productId: Int, date: LocalDate): ReadableDataFile[ShopRevenue] = {
        new ProductRevenueFile.Reader(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getProductRevenueWriter(productId: Int, date: LocalDate): WritableDataFile[ShopRevenue] = {
        new ProductRevenueFile.Writer(productId, date, ioService)
    }


}
