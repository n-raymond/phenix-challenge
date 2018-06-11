package phenix.dataFiles.impl.specifics

import java.time.LocalDate

import phenix.dataFiles.impl.DataFileImpl.LocatedInDataImpl
import phenix.dataFiles.impl.{DataFileImpl, ReadableDataFileImpl}
import phenix.models.Transaction

/**
  * A helper to manage files containing a transactions.
  * @param date The date of the DatedDataFile
  */
class TransactionFileReader(date: LocalDate)
    extends DataFileImpl[Transaction](date)
        with ReadableDataFileImpl[Transaction]
        with LocatedInDataImpl[Transaction] {



    /** @inheritdoc */
    override def fileNamePrefix: String = "transactions"

    /** @inheritdoc */
    override def deserializeData(serializedData: String): Transaction = phenix.models.Transaction(serializedData)

    /** @inheritdoc */
    override def serializeData(data: Transaction): String = data.toString

}
