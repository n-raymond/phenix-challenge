package phenix.dataFiles.specifics

import java.time.LocalDate

import phenix.dataFiles.general.{DataFileImpl, ReadableDataFileImpl}
import phenix.dataFiles.general.DataFileImpl.LocatedInDataImpl
import phenix.io.IOService
import phenix.models.Transaction

/**
  * A helper to manage files containing a transactions.
  * @param date The date of the DatedDataFile
  * @param ioService The injected service in charge of IO operations
  */
class TransactionFileReader(date: LocalDate, ioService: IOService)
    extends DataFileImpl[Transaction](date, ioService)
        with ReadableDataFileImpl[Transaction]
        with LocatedInDataImpl[Transaction] {

    /** @inheritdoc */
    override def fileNamePrefix: String = "transactions"

    /** @inheritdoc */
    override def deserializeData(serializedData: String): Transaction = phenix.models.Transaction(serializedData)

    /** @inheritdoc */
    override def serializeData(data: Transaction): String = data.toString

}
