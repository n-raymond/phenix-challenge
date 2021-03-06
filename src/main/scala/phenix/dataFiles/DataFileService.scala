package phenix.dataFiles

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.{ReadableDataFile, WritableDataFile}
import phenix.models._

/**
  * A service containing several factories to produce several kinds
  * of DataFile.
  */
trait DataFileService {

    /* Transaction */

    /**
      * A factory generating a reader of file containing transactions.
      *
      * @param date      The date of the file
      * @return          A ReadableDataFile of Transaction
      */
    def getTransactionReader(date: LocalDate): ReadableDataFile[Transaction]


    /* Reference */

    /**
      * A factory generating a reader of a file containing the product reference
      * and pricing of a shop.
      *
      * @param shop      The shop id
      * @param date      The date of the file
      * @return          A ReadableDataFile of ProductValue
      */
    def getReferenceReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductPrice]


    /* ShopQuantity */

    /**
      * A factory generating a reader of a file that binds the shops where a product
      * is sold to its sold quantities.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A ReadableDataFile of ShopQuantity
      */
    def getShopQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ShopQuantity]

    /**
      * A factory generating a writer of a file that binds the shops where a product
      * is sold to its sold quantities.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ShopQuantity
      */
    def getShopQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ShopQuantity]


    /* Intermediate ProductQuantity */

    /**
      * A factory generating a reader of an intermediate file used in the computing of the final file
      * that will bind shops to the sold quantities of a product.
      *
      * @param productId The id of the product concerned by the file
      * @param groupId   The intermediate group of the file
      * @param date      The date of the file
      * @return          An intermediate ReadableDataFile of ProductQuantity
      */
    def getInterShopQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ShopQuantity]

    /**
      * A factory generating a writer of an intermediate file used in the computing of the final file
      * that will bind shops to the sold quantities of a product.
      *
      * @param productId The id of the product concerned by the file
      * @param groupId   The intermediate group of the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ProductQuantity
      */
    def getInterShopQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ShopQuantity]


    /* ShopTopSellsFile */

    /**
      * A factory generating a shop top sells file reader.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getShopTopSellsReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductQuantity]

    /**
      * A factory generating a shop top sells file writer.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getShopTopSellsWriter(shop: UUID, date: LocalDate): WritableDataFile[ProductQuantity]



    /* IntermediateShopTopSellsFile */

    /**
      * A factory generating an intermediate shop top sells file reader.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getIntermediateShopTopSellsReader(shop: UUID, groupId: Int, date: LocalDate): ReadableDataFile[ProductQuantity]

    /**
      * A factory generating an intermediate shop top sells file writer.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getIntermediateShopTopSellsWriter(shop: UUID, groupId: Int, date: LocalDate): WritableDataFile[ProductQuantity]



    /* ProductPrice */

    /**
      * A factory generating a product price file reader.
      *
      * @param shop The shop id
      * @param productId: the productId
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getPriceReader(shop: UUID, productId: Int, date: LocalDate): ReadableDataFile[Double]

    /**
      * A factory generating a product price file writer.
      *
      * @param shop The shop id
      * @param productId: the productId
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getPriceWriter(shop: UUID, productId: Int, date: LocalDate): WritableDataFile[Double]


    /* ProductRevenue */

    /**
      * A factory generating a ReadableDataFile of product revenue.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A ReadableDataFile of ProductQuantity
      */
    def getShopRevenueReader(productId: Int, date: LocalDate): ReadableDataFile[ShopRevenue]

    /**
      * A factory generating a WritableDataFile of product revenue.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ProductQuantity
      */
    def getShopRevenueWriter(productId: Int, date: LocalDate): WritableDataFile[ShopRevenue]


    /* ShopTopRevenueFile */

    /**
      * A factory generating a shop top sells file reader.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductRevenue
      */
    def getShopTopRevenueReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductRevenue]

    /**
      * A factory generating a shop top sells file writer.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductRevenue
      */
    def getShopTopRevenueWriter(shop: UUID, date: LocalDate): WritableDataFile[ProductRevenue]



    /* IntermediateShopTopRevenueFile */

    /**
      * A factory generating an intermediate shop top revenue file reader.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductPrice
      */
    def getIntermediateShopTopRevenueReader(shop: UUID, groupId: Int, date: LocalDate): ReadableDataFile[ProductRevenue]

    /**
      * A factory generating an intermediate shop top revenue file writer.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductPrice
      */
    def getIntermediateShopTopRevenueWriter(shop: UUID, groupId: Int, date: LocalDate): WritableDataFile[ProductRevenue]

}
