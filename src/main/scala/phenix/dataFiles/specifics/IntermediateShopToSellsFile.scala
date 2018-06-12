package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.{ReadableDataFileImpl, WritableDataFileImpl}
import phenix.io.IOService
import phenix.models.ProductValue

/**
  * A helper to manage intermediate files of shop's top sells.
  * @param shop The id of the shop
  * @param group The intermediate group id of the file
  * @param date The date of the DatedDataFile
  * @param ioService
  */
class IntermediateShopToSellsFile(shop: UUID, val group: Int, date: LocalDate, ioService: IOService)
    extends ShopTopSellsFile(shop, date, ioService) {

    /** @inheritdoc*/
    override def fileNamePrefix: String = s"intermediate_top_100_ventes_${shop}_$group"

    /** @inheritdoc*/
    override def fileLocation: String = s"${super.fileLocation}/inter"

}

object IntermediateShopToSellsFile {

    class Reader(shop: UUID, group: Int, date: LocalDate, ioService: IOService)
        extends IntermediateShopToSellsFile(shop, group, date, ioService)
            with ReadableDataFileImpl[ProductValue]


    class Writer(shop: UUID, group: Int, date: LocalDate, ioService: IOService)
        extends IntermediateShopToSellsFile(shop, group, date, ioService)
            with WritableDataFileImpl[ProductValue]

}