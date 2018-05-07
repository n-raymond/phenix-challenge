package phenix.dataFiles

import java.time.LocalDate
import java.util.UUID

import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import phenix.io.{FileReader, FileWriter}
import phenix.models.ProductQuantity

import scala.util.Success

class ProductQuantityFileSpec extends FlatSpec with Matchers with MockFactory {

    private val conf = ConfigFactory.load()

    "fileName" should "return a valid file name" in {
        val file = new ProductQuantityFile(37, LocalDate.of(2015, 5, 14))

        file.fileName should equal (s"${conf.getString("paths.result")}/product_quantity/product_qty_37_20150514.data")
    }

    "ProductQuantityFile.Reader" should "be able to read data from file" in {
        val file = new ProductQuantityFile.Reader(37, LocalDate.of(2015, 5, 14))

        val fileData = Iterator(
            "72a2876c-bc8b-4f35-8882-8d661fac2606|652",
            "29366c83-eae9-42d3-a8af-f15339830dc5|8",
            "8e588f2f-d19e-436c-952f-1cdd9f0b12b0|12"
        )

        val fileReader = stub[FileReader]
        file.fileReader = fileReader
        fileReader.readLines _ when() returns fileData

        val result = Iterable(
            Success(ProductQuantity(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 652)),
            Success(ProductQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 8)),
            Success(ProductQuantity(UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"), 12))
        )

        file.getContent should equal (result)
    }

    "ProductQuantityFile.Writer" should "be able to write data to file" in {
        val file = new ProductQuantityFile.Writer(37, LocalDate.of(2015, 5, 14))

        val expected = Iterable(
            "72a2876c-bc8b-4f35-8882-8d661fac2606|652",
            "29366c83-eae9-42d3-a8af-f15339830dc5|8",
            "8e588f2f-d19e-436c-952f-1cdd9f0b12b0|12"
        )

        val data = Iterable(
            ProductQuantity(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 652),
            ProductQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 8),
            ProductQuantity(UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"), 12)
        )

        val fileWriter = mock[FileWriter]
        file.fileWriter = fileWriter
        fileWriter.writeLines _ expects expected

        file.writeData(data)
    }


}
