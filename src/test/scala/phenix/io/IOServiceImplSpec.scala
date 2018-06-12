package phenix.io

import org.scalatest.{FlatSpec, Matchers}
import phenix.io.reader.BufferedSourceAdapter
import phenix.io.writer.PrintWriterAdapter

class IOServiceImplSpec extends FlatSpec with Matchers {

    val ioService = new IOServiceImpl

    "getFileReader" should "return an instance of BufferedSourceAdapter" in {
        ioService.getFileReader("aNiceFile.txt").getClass should equal (new BufferedSourceAdapter("aNiceFile.txt").getClass)
    }

    "getFileWriter" should "return an instance of PrintWriterAdapter" in {
        ioService.getFileWriter("aNiceFile.txt").getClass should equal (new PrintWriterAdapter("aNiceFile.txt").getClass)
    }


}
