package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.{ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ProductPrice

/**
  * A helper to manage intermediate files of shop's top sells.
  * @param shop The id of the shop
  * @param group The intermediate group id of the file
  * @param date The date of the DatedDataFile
  * @param ioService
  */
class IntermediateShopTopRevenueFile(shop: UUID, val group: Int, date: LocalDate, ioService: IOService)
    extends ShopTopRevenueFile(shop, date, ioService) {

    /** @inheritdoc*/
    override def fileNamePrefix: String = s"intermediate_top_100_ca_${shop}_$group"

    /** @inheritdoc*/
    override def fileLocation: String = s"${super.fileLocation}/inter"

}

object IntermediateShopTopRevenueFile {

    class Reader(shop: UUID, group: Int, date: LocalDate, ioService: IOService)
        extends IntermediateShopTopRevenueFile(shop, group, date, ioService)
            with ReadableDataFileImpl[ProductPrice]


    class Writer(shop: UUID, group: Int, date: LocalDate, ioService: IOService)
        extends IntermediateShopTopRevenueFile(shop, group, date, ioService)
            with WritableDataFileImpl[ProductPrice]

}