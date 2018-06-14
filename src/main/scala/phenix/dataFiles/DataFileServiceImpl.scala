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
    override def getShopQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ShopQuantity] = {
        new ShopQuantityFile.Reader(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getShopQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ShopQuantity] = {
        new ShopQuantityFile.Writer(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getInterShopQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ShopQuantity] = {
        new IntermediateShopQuantityFile.Reader(productId, groupId, date, ioService)
    }

    /** @inheritdoc */
    override def getInterShopQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ShopQuantity] = {
        new IntermediateShopQuantityFile.Writer(productId, groupId, date, ioService)
    }

    /** @inheritdoc */
    override def getShopTopSellsReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductQuantity] = {
        new ShopTopQuantityFile.Reader(shop, date, ioService)
    }

    /** @inheritdoc */
    override def getShopTopSellsWriter(shop: UUID, date: LocalDate): WritableDataFile[ProductQuantity] = {
        new ShopTopQuantityFile.Writer(shop, date, ioService)
    }

    /** @inheritdoc */
    override def getIntermediateShopTopSellsReader(shop: UUID, group: Int, date: LocalDate): ReadableDataFile[ProductQuantity] = {
        new IntermediateShopTopQuantityFile.Reader(shop, group, date, ioService)
    }

    /** @inheritdoc */
    override def getIntermediateShopTopSellsWriter(shop: UUID, group: Int, date: LocalDate): WritableDataFile[ProductQuantity] = {
        new IntermediateShopTopQuantityFile.Writer(shop, group, date, ioService)
    }

    /** @inheritdoc */

    override def getPriceReader(shop: UUID, productId: Int, date: LocalDate): ReadableDataFile[Double] = {
        new PriceFile.Reader(shop, productId, date, ioService)
    }

    /** @inheritdoc */
    override def getPriceWriter(shop: UUID, productId: Int, date: LocalDate): WritableDataFile[Double] = {
        new PriceFile.Writer(shop, productId, date, ioService)
    }

    /** @inheritdoc */
    override def getShopRevenueReader(productId: Int, date: LocalDate): ReadableDataFile[ShopRevenue] = {
        new ShopRevenueFile.Reader(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getShopRevenueWriter(productId: Int, date: LocalDate): WritableDataFile[ShopRevenue] = {
        new ShopRevenueFile.Writer(productId, date, ioService)
    }

    /** @inheritdoc */
    override def getShopTopRevenueReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductRevenue] = {
        new ShopTopRevenueFile.Reader(shop, date, ioService)
    }

    /** @inheritdoc */
    override def getShopTopRevenueWriter(shop: UUID, date: LocalDate): WritableDataFile[ProductRevenue] = {
        new ShopTopRevenueFile.Writer(shop, date, ioService)
    }

    /** @inheritdoc */
    override def getIntermediateShopTopRevenueReader(shop: UUID, groupId: Int, date: LocalDate): ReadableDataFile[ProductRevenue] = {
        new IntermediateShopTopRevenueFile.Reader(shop, groupId, date, ioService)
    }

    /** @inheritdoc */
    override def getIntermediateShopTopRevenueWriter(shop: UUID, groupId: Int, date: LocalDate): WritableDataFile[ProductRevenue] = {
        new IntermediateShopTopRevenueFile.Writer(shop, groupId, date, ioService)
    }
}
