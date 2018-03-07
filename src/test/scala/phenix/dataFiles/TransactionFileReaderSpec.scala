package phenix.dataFiles

import org.scalatest._
import org.scalamock.scalatest.MockFactory
import java.time.LocalDate
import java.util.UUID

import phenix.io.FileReader
import phenix.models.{Transaction}
import scala.util.{Failure, Success}

class TransactionFileReaderSpec extends FlatSpec with Matchers with MockFactory {

    /* Initialization */

    val transactionFile = new TransactionFileReader(LocalDate.of(2015, 5, 14))

    val smallFileContent = Iterator(
        "1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5",
        "1|20170514T224008+0100|bdc2a431-797d-4b07-9567-67c565a67b84|55|3",
        "1|20170514T224214+0100|72a2876c-bc8b-4f35-8882-8d661fac2606|39|8",
        "2|20170514T225357+0100|29366c83-eae9-42d3-a8af-f15339830dc5|10|6"
    )

    val smallInvalidFileContent = Iterator(
        "1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5",
        "1|20170514T224008+0100|bdc-9567-67c565a67b84|55|3",
        "1|20170514T224214+0100|72a2876c-bc8b-4f35-8882-8d661fac2606|39|8",
        "2|20170514T225357+0100|29366c83-eae9-42d3-a8af-f15339830dc5|10|6"
    )

    val largeFileContent = Iterator(
        "1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5",
        "1|20170514T224008+0100|bdc2a431-797d-4b07-9567-67c565a67b84|55|3",
        "1|20170514T224214+0100|72a2876c-bc8b-4f35-8882-8d661fac2606|39|8",
        "2|20170514T225357+0100|29366c83-eae9-42d3-a8af-f15339830dc5|10|6",
        "2|20170514T230721+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|773|2",
        "2|20170514T231647+0100|bdc2a431-797d-4b07-9567-67c565a67b84|759|1",
        "3|20170514T232938+0100|bf0999da-ae45-49df-983e-89020198330b|789|6",
        "3|20170514T234000+0100|6af0502b-ce7a-4a6f-b5d3-516d09514095|520|5",
        "3|20170514T234640+0100|29366c83-eae9-42d3-a8af-f15339830dc5|733|3",
        "3|20170514T235351+0100|dd43720c-be43-41b6-bc4a-ac4beabd0d9b|985|9",
        "3|20170514T000050+0100|bf0999da-ae45-49df-983e-89020198330b|307|1",
        "3|20170514T001547+0100|6af0502b-ce7a-4a6f-b5d3-516d09514095|563|7",
        "4|20170514T001635+0100|af068240-8198-4b79-9cf9-c28c0db65f63|733|8",
        "4|20170514T001723+0100|72a2876c-bc8b-4f35-8882-8d661fac2606|54|1",
        "4|20170514T001944+0100|af068240-8198-4b79-9cf9-c28c0db65f63|3|3",
        "4|20170514T002931+0100|bdc2a431-797d-4b07-9567-67c565a67b84|124|8",
        "4|20170514T003551+0100|6af0502b-ce7a-4a6f-b5d3-516d09514095|562|1",
        "5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3",
        "6|20170514T005731+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|617|0",
        "6|20170514T010831+0100|29366c83-eae9-42d3-a8af-f15339830dc5|617|3"
    )

    val fileReader = stub[FileReader]

    /* Tests */

    "fileName" should "return a valid file name according to the date" in {
        transactionFile.fileName should equal ("transactions_20150514.data")
    }

    "getContent" should "return a valid stream containing deserialized data" in {
        (fileReader.getFileLines _) when() returns(smallFileContent)

        val expected = Stream(
            Success(new Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 531, 5)),
            Success(new Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 55, 3)),
            Success(new Transaction(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 39, 8)),
            Success(new Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 10, 6))
        )

        transactionFile.getContent(fileReader) should equal (expected)
    }

    it should "return a stream containing deserialized data with a Failed value in second place and Success in other positions" in {
        (fileReader.getFileLines _) when() returns(smallInvalidFileContent)

        val result = transactionFile.getContent(fileReader)

        result(0) shouldBe a [Success[_]]
        result(1) shouldBe a [Failure[_]]
        result(2) shouldBe a [Success[_]]
        result(3) shouldBe a [Success[_]]
    }

    "getChunks" should "return a valid stream of chunks" in {
        (fileReader.getFileLines _) when() returns(largeFileContent)

        val result = transactionFile.getChunks(5)(fileReader)

        val expected = Stream(
            (0, Stream(
                Success(new Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 531, 5)),
                Success(new Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 55, 3)),
                Success(new Transaction(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 39, 8)),
                Success(new Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 10, 6)),
                Success(new Transaction(UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"), 773, 2))
            )),
            (1, Stream(
                Success(new Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 759, 1)),
                Success(new Transaction(UUID.fromString("bf0999da-ae45-49df-983e-89020198330b"), 789, 6)),
                Success(new Transaction(UUID.fromString("6af0502b-ce7a-4a6f-b5d3-516d09514095"), 520, 5)),
                Success(new Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 733, 3)),
                Success(new Transaction(UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"), 985, 9))
            )),
            (2, Stream(
                Success(new Transaction(UUID.fromString("bf0999da-ae45-49df-983e-89020198330b"), 307, 1)),
                Success(new Transaction(UUID.fromString("6af0502b-ce7a-4a6f-b5d3-516d09514095"), 563, 7)),
                Success(new Transaction(UUID.fromString("af068240-8198-4b79-9cf9-c28c0db65f63"), 733, 8)),
                Success(new Transaction(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 54, 1)),
                Success(new Transaction(UUID.fromString("af068240-8198-4b79-9cf9-c28c0db65f63"), 3, 3))
            )),
            (3, Stream(
                Success(new Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 124, 8)),
                Success(new Transaction(UUID.fromString("6af0502b-ce7a-4a6f-b5d3-516d09514095"), 562, 1)),
                Success(new Transaction(UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"), 266, 3)),
                Success(new Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 617, 0)),
                Success(new Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 617,  3))
            ))
        )

        result should equal (expected)
    }

}
