package phenix.dataFiles

import java.time.LocalDate

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import phenix.dataFiles.specifics.{IntermediateProductQuantityFile, ProductQuantityFile}

class DataFileServiceImplSpec extends FlatSpec with Matchers with MockFactory {

    private val dataFileService = new DataFileServiceImpl

    "getProductQuantityReader" should "return an instance of ProductQuantityFile.Reader" in {
        val result = dataFileService.getProductQuantityReader(1, LocalDate.of(2015, 5, 4))
        val expected = new ProductQuantityFile.Reader(1, LocalDate.of(2015, 5, 4))
        result.getClass should equal (expected.getClass)
    }

    "getProductQuantityWriter" should "return an instance of ProductQuantityFile.Writer" in {
        val result = dataFileService.getProductQuantityWriter(1, LocalDate.of(2015, 5, 4))
        val expected = new ProductQuantityFile.Writer(1, LocalDate.of(2015, 5, 4))
        result.getClass should equal (expected.getClass)
    }

    "getInterProductQuantityReader" should "return an instance of IntermediateProductQuantityFile.Reader" in {
        val result = dataFileService.getInterProductQuantityReader(1, 2, LocalDate.of(2015, 5, 4))
        val expected = new IntermediateProductQuantityFile.Reader(1, 2, LocalDate.of(2015, 5, 4))
        result.getClass should equal (expected.getClass)
    }

    "getInterProductQuantityWriter" should "return an instance of IntermediateProductQuantityFile.Writer" in {
        val result = dataFileService.getInterProductQuantityWriter(1, 2, LocalDate.of(2015, 5, 4))
        val expected = new IntermediateProductQuantityFile.Writer(1, 2, LocalDate.of(2015, 5, 4))
        result.getClass should equal (expected.getClass)
    }
}
