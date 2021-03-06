package phenix.dataFiles.specifics

import java.time.LocalDate
import java.util.UUID

import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import phenix.io.IOService
import phenix.io.reader.FileReader
import phenix.io.writer.FileWriter
import phenix.models.ShopQuantity

import scala.util.Success

class ShopQuantityFileSpec extends FlatSpec with Matchers with MockFactory {

    private val conf = ConfigFactory.load()

    private val ioService = stub[IOService]

    private val fileName = s"${conf.getString("paths.result")}/product_quantity/product_qty_37_20150514.data"

    "fileName" should "return a valid file name" in {
        val file = new ShopQuantityFile(37, LocalDate.of(2015, 5, 14), ioService)

        file.fileName should equal (fileName)
    }

    "ProductQuantityFile.Reader" should "be able to read data from file" in {

        val fileData = Iterator(
            "72a2876c-bc8b-4f35-8882-8d661fac2606|652",
            "29366c83-eae9-42d3-a8af-f15339830dc5|8",
            "8e588f2f-d19e-436c-952f-1cdd9f0b12b0|12"
        )

        val fileReader = stub[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.readLines _ when() returns fileData

        val file = new ShopQuantityFile.Reader(37, LocalDate.of(2015, 5, 14), ioService)


        val result = Iterable(
            Success(ShopQuantity(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 652)),
            Success(ShopQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 8)),
            Success(ShopQuantity(UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"), 12))
        )

        file.getContent should equal (result)
    }

    "ProductQuantityFile.Writer" should "be able to write data to file" in {

        val expected = Iterable(
            "72a2876c-bc8b-4f35-8882-8d661fac2606|652",
            "29366c83-eae9-42d3-a8af-f15339830dc5|8",
            "8e588f2f-d19e-436c-952f-1cdd9f0b12b0|12"
        )

        val fileWriter = mock[FileWriter]
        ioService.getFileWriter _ when fileName returns fileWriter
        fileWriter.writeLines _ expects expected

        val file = new ShopQuantityFile.Writer(37, LocalDate.of(2015, 5, 14), ioService)

        val data = Iterable(
            ShopQuantity(UUID.fromString("72a2876c-bc8b-4f35-8882-8d661fac2606"), 652),
            ShopQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 8),
            ShopQuantity(UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"), 12)
        )

        file.writeData(data)
    }


    "serialiseData" should "well serialize a ProductQuantity" in {
        val file = new ShopQuantityFile(1, LocalDate.of(2015, 5, 14), ioService)
        val result = file.serializeData(ShopQuantity(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 531))

        result should equal ("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531")
    }


}
