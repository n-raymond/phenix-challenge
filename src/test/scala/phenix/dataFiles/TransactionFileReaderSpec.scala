package phenix.dataFiles

import org.scalatest._
import org.scalamock.scalatest.MockFactory
import java.time.LocalDate
import java.util.UUID

import com.typesafe.config.ConfigFactory
import phenix.io.FileReader
import phenix.models.Transaction

import scala.util.{Failure, Success}

class TransactionFileReaderSpec extends FlatSpec with Matchers with MockFactory {

    private val conf = ConfigFactory.load()

    "fileName" should "return a valid file name according to the date" in {
        val transactionFile = new TransactionFileReader(LocalDate.of(2015, 5, 14))
        transactionFile.fileName should equal (s"${conf.getString("paths.data")}/transactions_20150514.data")
    }

    "getContent" should "return a valid stream containing deserialized data" in {
        val transactionFile = new TransactionFileReader(LocalDate.of(2015, 5, 14))

        val smallFileContent = Iterator(
            "1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5",
            "1|20170514T224008+0100|bdc2a431-797d-4b07-9567-67c565a67b84|55|3",
            "1|20170514T224214+0100|72a2876c-bc8b-4f35-8882-8d661fac2606|39|8",
            "2|20170514T225357+0100|29366c83-eae9-42d3-a8af-f15339830dc5|10|6"
        )

        val fileReader = stub[FileReader]
        transactionFile.fileReader = fileReader
        fileReader.readLines _ when() returns smallFileContent

        val expected = Stream(
            Success(new Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 531, 5)),
            Success(new Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 55, 3)),
            Success(new Transaction(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 39, 8)),
            Success(new Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 10, 6))
        )

        transactionFile.getContent should equal (expected)
    }

    "serialise" should "well serialize a Transaction" in {
        val transactionFile = new TransactionFileReader(LocalDate.of(2015, 5, 14))
        val result = transactionFile.serializeData(Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 531, 5))

        result should equal ("0|20000101T000000+01000|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5")
    }

}
