package phenix.dataFiles

import java.time.LocalDate

import phenix.models.Transaction

/**
  * A helper to manage files containing a transactions.
  * @param date The date of the DatedDataFile
  */
class TransactionFileReader(date: LocalDate) extends DatedDataFile(date) with DatedDataFile.Readable {

    /** @inheritdoc */
    override type Data = Transaction

    /** @inheritdoc */
    override def fileNamePrefix: String = "transactions"

    /** @inheritdoc */
    override def deserializeData(serializedData: String): Transaction = Transaction(serializedData)

    /** @inheritdoc */
    override def serializeData(data: Transaction): String = data.toString

}
