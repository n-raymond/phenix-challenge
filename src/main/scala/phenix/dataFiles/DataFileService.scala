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
      * A factory generating a ReadableDataFile of Transaction.
      *
      * @param date      The date of the file
      * @return          A ReadableDataFile of Transaction
      */
    def getTransactionReader(date: LocalDate): ReadableDataFile[Transaction]

    /* Reference */


    /**
      * A factory generating a ReadableDataFile for a shop reference.
      *
      * @param shop      The shop id
      * @param date      The date of the file
      * @return          A ReadableDataFile of ProductValue
      */
    def getReferenceReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductPrice]

    /* ProductQuantity */

    /**
      * A factory generating a ReadableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A ReadableDataFile of ProductQuantity
      */
    def getProductQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ShopQuantity]

    /**
      * A factory generating a WritableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ProductQuantity
      */
    def getProductQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ShopQuantity]


    /* Intermediate ProductQuantity */

    /**
      * A factory generating an intermediate ReadableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param groupId   The intermediate group of the file
      * @param date      The date of the file
      * @return          An intermediate ReadableDataFile of ProductQuantity
      */
    def getInterProductQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ShopQuantity]

    /**
      * A factory generating an intermediate WritableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param groupId   The intermediate group of the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ProductQuantity
      */
    def getInterProductQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ShopQuantity]


    /* ShopTopSellsFile */

    /**
      * A factory generating a shop top sells file reader.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getShopTopSellsReader(shop: UUID, date: LocalDate): ReadableDataFile[ProductValue]

    /**
      * A factory generating a shop top sells file writer.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getShopTopSellsWriter(shop: UUID, date: LocalDate): WritableDataFile[ProductValue]



    /* IntermediateShopTopSellsFile */

    /**
      * A factory generating an intermediate shop top sells file reader.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getIntermediateShopTopSellsReader(shop: UUID, groupId: Int, date: LocalDate): ReadableDataFile[ProductValue]

    /**
      * A factory generating an intermediate shop top sells file writer.
      *
      * @param shop The shop id
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getIntermediateShopTopSellsWriter(shop: UUID, groupId: Int, date: LocalDate): WritableDataFile[ProductValue]



    /* ProductPrice */

    /**
      * A factory generating a product price file reader.
      *
      * @param shop The shop id
      * @param productId: the productId
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getProductPriceReader(shop: UUID, productId: Int, date: LocalDate): ReadableDataFile[Double]

    /**
      * A factory generating an intermediate shop top sells file writer.
      *
      * @param shop The shop id
      * @param productId: the productId
      * @param date The date of the file
      * @return     An intermediate ReadableDataFile of ProductQuantity
      */
    def getProductPriceWriter(shop: UUID, productId: Int, date: LocalDate): WritableDataFile[Double]


    /* ProductRevenue */

    /**
      * A factory generating a ReadableDataFile of product revenue.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A ReadableDataFile of ProductQuantity
      */
    def getProductRevenueReader(productId: Int, date: LocalDate): ReadableDataFile[ShopRevenue]

    /**
      * A factory generating a WritableDataFile of product revenue.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ProductQuantity
      */
    def getProductRevenueWriter(productId: Int, date: LocalDate): WritableDataFile[ShopRevenue]

}
