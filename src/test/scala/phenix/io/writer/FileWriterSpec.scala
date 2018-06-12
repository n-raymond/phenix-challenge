package phenix.io.writer

import org.scalatest.{FlatSpec, Matchers}

class FileWriterSpec extends FlatSpec with Matchers {

    "apply" should "return an instance of PrintWriterAdapter" in {

        FileWriter.apply("aNiceFile.txt").getClass should equal (new PrintWriterAdapter("aNiceFile.txt").getClass)

    }

}
