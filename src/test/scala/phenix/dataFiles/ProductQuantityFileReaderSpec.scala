package phenix.dataFiles

import java.time.LocalDate
import java.util.UUID

import org.scalamock.scalatest.MockFactory
import org.scalatest._
import phenix.dataFiles
import phenix.io.FileReader
import phenix.models.{ProductQuantity}

import scala.util.{Failure, Success}

class ProductQuantityFileReaderSpec extends FlatSpec with Matchers with MockFactory {

    /* Initialization */

    val pqtyFile = new dataFiles.ProductQuantityFile.Reader(19, LocalDate.of(2015, 5, 14))

    val smallFileContent = Iterator(
        "2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531",
        "bdc2a431-797d-4b07-9567-67c565a67b84|55",
        "72a2876c-bc8b-4f35-8882-8d661fac2606|39",
        "29366c83-eae9-42d3-a8af-f15339830dc5|10"
    )

    val smallInvalidFileContent = Iterator(
        "2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531",
        "bdc-9567-67c565a67b84|55",
        "72a2876c-bc8b-4f35-8882-8d661fac2606|39",
        "29366c83-eae9-42d3-a8af-f15339830dc5|10"
    )

    val fileReader = stub[FileReader]

    /* Tests */

    "fileName" should "return a valid file name according to the date" in {
        pqtyFile.fileName should equal ("product_qty_19_20150514.data")
    }

    "getContent" should "return a valid stream containing deserialized data" in {
        (fileReader.getFileLines _) when() returns(smallFileContent)

        val expected = Stream(
            Success(new ProductQuantity(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 531)),
            Success(new ProductQuantity(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 55)),
            Success(new ProductQuantity(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 39)),
            Success(new ProductQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 10))
        )

        pqtyFile.getContent(fileReader) should equal (expected)
    }

    it should "return a stream containing deserialized data with a Failed value in second place and Success in other positions" in {
        (fileReader.getFileLines _) when() returns(smallInvalidFileContent)

        val result = pqtyFile.getContent(fileReader)

        result(0) shouldBe a [Success[_]]
        result(1) shouldBe a [Failure[_]]
        result(2) shouldBe a [Success[_]]
        result(3) shouldBe a [Success[_]]
    }

}
