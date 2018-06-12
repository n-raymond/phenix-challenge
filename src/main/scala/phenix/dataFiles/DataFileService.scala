package phenix.dataFiles

import java.time.LocalDate

import com.typesafe.config.ConfigFactory
import phenix.dataFiles.general.{ReadableDataFile, WritableDataFile}
import phenix.models.{ProductQuantity, Transaction}

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


    /* ProductQuantity */

    /**
      * A factory generating a ReadableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A ReadableDataFile of ProductQuantity
      */
    def getProductQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ProductQuantity]

    /**
      * A factory generating a WritableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ProductQuantity
      */
    def getProductQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ProductQuantity]


    /* Intermediate ProductQuantity */

    /**
      * A factory generating an intermediate ReadableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param groupId   The intermediate group of the file
      * @param date      The date of the file
      * @return          An intermediate ReadableDataFile of ProductQuantity
      */
    def getInterProductQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ProductQuantity]

    /**
      * A factory generating an intermediate WritableDataFile of ProductQuantity.
      *
      * @param productId The id of the product concerned by the file
      * @param groupId   The intermediate group of the file
      * @param date      The date of the file
      * @return          A WritableDataFile of ProductQuantity
      */
    def getInterProductQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ProductQuantity]

}
