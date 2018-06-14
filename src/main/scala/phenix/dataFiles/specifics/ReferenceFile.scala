package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import phenix.dataFiles.general.DataFileImpl.LocatedInDataImpl
import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl}
import phenix.io.IOService
import phenix.models.ProductPrice
import phenix.utils.RegexUUID

import scala.util.matching.Regex


/**
  * A helper to manage files containing a shop reference.
  * @param shop The shop referenced by the file
  * @param date The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class ReferenceFile(val shop: UUID, date: LocalDate, ioService: IOService)
    extends DataFileImpl[ProductPrice](date, ioService)
        with ReadableDataFileImpl[ProductPrice]
        with LocatedInDataImpl[ProductPrice] {

    /** @inheritdoc*/
    override def fileNamePrefix: String = s"${ReferenceFile.referenceProdName}$shop"

    /** @inheritdoc*/
    override def deserializeData(serializedData: String): ProductPrice = ProductPrice(serializedData)

    /** @inheritdoc*/
    override def serializeData(data: ProductPrice): String = data.toString

}

object ReferenceFile extends RegexUUID{

    val referenceProdName = "reference_prod-"

    val fileNameRegex : Regex = s"reference_prod-${regexUUID}_[0-9]{8}.data".r

    def getUUIDFromFileName(fileName: String) = UUID.fromString(fileName.substring(15, 51))

    def getDateFromFilename(fileName: String) = {
        LocalDate.of(
            fileName.substring(52, 56).toInt,
            fileName.substring(56,58).toInt,
            fileName.substring(58,60).toInt
        )
    }


}