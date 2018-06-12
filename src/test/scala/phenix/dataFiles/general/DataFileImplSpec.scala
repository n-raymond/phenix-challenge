package phenix.dataFiles.general

import java.time.LocalDate

import org.scalamock.scalatest.MockFactory
import org.scalatest._
import phenix.io.IOService
import phenix.io.reader.FileReader
import phenix.io.writer.FileWriter

import scala.util.{Failure, Success}

class DataFileImplSpec extends FlatSpec with Matchers with MockFactory {

    private val ioService = stub[IOService]

    /* A simple implementation */

    class IntDataFileImpl(date: LocalDate) extends DataFileImpl[Int](date, ioService) {
        override protected def fileNamePrefix: String = "int"
        override def deserializeData(serializedData: String): Int = serializedData.toInt
        override def serializeData(data: Int): String = data.toString
        override def fileLocation: String = "test"
    }

    class ReadableIntDataFileImpl(date: LocalDate) extends IntDataFileImpl(date) with ReadableDataFileImpl[Int]

    class WritableIntDataFileImpl(date: LocalDate) extends IntDataFileImpl(date) with WritableDataFileImpl[Int]


    /* Data */

    private val smallFileContent = Iterator("1", "2", "3", "4")

    private val smallInvalidFileContent = Iterator("1", "a", "2", "3")

    private val largeFileContent = Range.inclusive(1, 20).map(_.toString).toIterator

    private val fileName = "test/int_20150514.data"


    /* Tests */

    "fileName" should "return a valid file name according to the date" in {
        val file = new IntDataFileImpl(LocalDate.of(2015, 5, 14))
        file.fileName should equal (fileName)
    }

    "Readable.getContent" should "return a valid stream containing deserialized data" in {
        val fileReader = stub[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.readLines _ when() returns smallFileContent

        val file = new ReadableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        val expected = Stream(
            Success(1),
            Success(2),
            Success(3),
            Success(4)
        )

        file.getContent should equal (expected)
    }

    it should "return a stream containing deserialized data with a Failed value in second place and Success in other positions" in {
        val fileReader = stub[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.readLines _ when() returns smallInvalidFileContent

        val file = new ReadableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        val result = file.getContent

        result.head shouldBe a [Success[_]]
        result(1) shouldBe a [Failure[_]]
        result(2) shouldBe a [Success[_]]
        result(3) shouldBe a [Success[_]]
    }

    it should "open the reader" in {
        val fileReader = mock[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.readLines _ expects() returning smallFileContent

        val file = new ReadableIntDataFileImpl(LocalDate.of(2015, 5, 14))
        file.isOpen should equal (false)

        file.getContent
        file.isOpen should equal (true)
    }

    it should "throw an IllegalStateException if called twice" in {
        val fileReader = mock[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.readLines _ expects() returning smallFileContent

        val file = new ReadableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        //First Try
        file.getContent

        an [IllegalStateException] should be thrownBy file.getContent
    }

    "Reader.getChunks" should "return a valid stream of chunks" in {
        val fileReader = stub[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.readLines _ when() returns largeFileContent

        val file = new ReadableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        val result = file.getChunks(5)

        val expected = Stream(
            (0, Stream(Success(1), Success(2), Success(3), Success(4), Success(5))),
            (1, Stream(Success(6), Success(7), Success(8), Success(9), Success(10))),
            (2, Stream(Success(11), Success(12), Success(13), Success(14), Success(15))),
            (3, Stream(Success(16), Success(17), Success(18), Success(19), Success(20)))
        )

        result should equal (expected)
    }

    it should "open the reader" in {
        val fileReader = mock[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.readLines _ expects() returning smallFileContent

        val file = new ReadableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        file.isOpen should equal (false)

        file.getChunks(5)

        file.isOpen should equal (true)
    }

    "Reader.close" should "call the close function of the fileReader if called" in {
        val fileReader = mock[FileReader]
        ioService.getFileReader _ when fileName returns fileReader
        fileReader.close _ expects()

        val file = new ReadableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        file.close()
    }

    "Writer.writeLines" should "send serialized data on the fileWriter" in {
        val fileWriter = mock[FileWriter]
        ioService.getFileWriter _ when fileName returns fileWriter
        fileWriter.writeLines _ expects Iterable("1", "2", "3", "4")

        val file = new WritableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        file.writeData(Stream(1, 2, 3, 4))
    }

    /*it should "create the folders if they do not exist" in {
        ???
    }*/

    it should "open the writer" in {
        val fileWriter = mock[FileWriter]
        ioService.getFileWriter _ when fileName returns fileWriter
        fileWriter.writeLines _ expects *

        val file = new WritableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        file.isOpen should equal (false)

        file.writeData(Stream(1, 2, 3, 4))

        file.isOpen should equal (true)
    }

    it should "throw an IllegalStateException if called twice" in {
        val fileWriter = mock[FileWriter]
        ioService.getFileWriter _ when fileName returns fileWriter
        fileWriter.writeLines _ expects *

        val file = new WritableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        //First Try
        file.writeData(Stream(1, 2, 3, 4))

        an [IllegalStateException] should be thrownBy file.writeData(Stream(1, 2, 3, 4))
    }

    "Writer.close" should "call the close function of the fileWriter if called" in {
        val fileWriter = mock[FileWriter]
        ioService.getFileWriter _ when fileName returns fileWriter
        fileWriter.close _ expects()

        val file = new WritableIntDataFileImpl(LocalDate.of(2015, 5, 14))

        file.close()
    }

}
