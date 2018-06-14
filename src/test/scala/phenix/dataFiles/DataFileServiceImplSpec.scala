package phenix.dataFiles

import java.time.LocalDate

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import phenix.dataFiles.specifics.{IntermediateShopQuantityFile, ShopQuantityFile, TransactionFileReader}
import phenix.io.IOService

class DataFileServiceImplSpec extends FlatSpec with Matchers with MockFactory {

    private val ioService = stub[IOService]

    private val dataFileService = new DataFileServiceImpl(ioService)

    "getTransactionReader" should "return an instance of TransactionFileReader" in {
        val result = dataFileService.getTransactionReader(LocalDate.of(2015, 5, 4))
        val expected = new TransactionFileReader(LocalDate.of(2015, 5, 4), ioService)
        result.getClass should equal (expected.getClass)
    }

    "getProductQuantityReader" should "return an instance of ProductQuantityFile.Reader" in {
        val result = dataFileService.getShopQuantityReader(1, LocalDate.of(2015, 5, 4))
        val expected = new ShopQuantityFile.Reader(1, LocalDate.of(2015, 5, 4), ioService)
        result.getClass should equal (expected.getClass)
    }

    "getProductQuantityWriter" should "return an instance of ProductQuantityFile.Writer" in {
        val result = dataFileService.getShopQuantityWriter(1, LocalDate.of(2015, 5, 4))
        val expected = new ShopQuantityFile.Writer(1, LocalDate.of(2015, 5, 4), ioService)
        result.getClass should equal (expected.getClass)
    }

    "getInterProductQuantityReader" should "return an instance of IntermediateProductQuantityFile.Reader" in {
        val result = dataFileService.getInterShopQuantityReader(1, 2, LocalDate.of(2015, 5, 4))
        val expected = new IntermediateShopQuantityFile.Reader(1, 2, LocalDate.of(2015, 5, 4), ioService)
        result.getClass should equal (expected.getClass)
    }

    "getInterProductQuantityWriter" should "return an instance of IntermediateProductQuantityFile.Writer" in {
        val result = dataFileService.getInterShopQuantityWriter(1, 2, LocalDate.of(2015, 5, 4))
        val expected = new IntermediateShopQuantityFile.Writer(1, 2, LocalDate.of(2015, 5, 4), ioService)
        result.getClass should equal (expected.getClass)
    }
}
