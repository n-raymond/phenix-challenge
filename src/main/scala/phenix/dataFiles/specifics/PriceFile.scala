package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.DataFileImpl.LocatedInResult
import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService

/**
  * A helper to manage files quantity of a product.
  *
  * @param productId The id of the product
  * @param date The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class PriceFile(val shop: UUID, val productId: Int, date: LocalDate, ioService: IOService)
    extends DataFileImpl[Double](date, ioService)
        with LocatedInResult[Double] {

    /** @inheritdoc */
    override def fileNamePrefix: String = s"product_price_${shop}_$productId"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): Double = serializedData.toDouble

    /** @inheritdoc */
    override def serializeData(data: Double): String = data.toString

    /** @inheritdoc */
    override def fileLocation: String = s"${super.fileLocation}/product_price"
}


object PriceFile {

    class Reader(shop: UUID, productId: Int, date: LocalDate, ioService: IOService)
        extends PriceFile(shop, productId, date, ioService)
            with ReadableDataFileImpl[Double]

    class Writer(shop: UUID, productId: Int, date: LocalDate, ioService: IOService)
        extends PriceFile(shop, productId, date, ioService)
            with WritableDataFileImpl[Double]

}
