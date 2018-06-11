package phenix.dataFiles

import java.time.LocalDate

import com.typesafe.config.ConfigFactory
import phenix.models.ProductQuantity
/**
  * An abstract factory to create several kind of DataFile.
  */
trait DataFileFactory {

    /**
      * Generates a ReadableDataFile[ProductQuantity]
      * @param date
      * @param productId
      * @return
      */
    def getProductQuantityReader(productId: Int, date: LocalDate): ReadableDataFile[ProductQuantity]

    /* TODO Comment me ! */
    def getProductQuantityWriter(productId: Int, date: LocalDate): WritableDataFile[ProductQuantity]


    /* TODO Comment me ! */
    def getInterProductQuantityReader(productId: Int, groupId: Int, date: LocalDate): ReadableDataFile[ProductQuantity]

    /* TODO Comment me ! */
    def getInterProductQuantityWriter(productId: Int, groupId: Int, date: LocalDate): WritableDataFile[ProductQuantity]

}
