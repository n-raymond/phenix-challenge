package phenix.io

import org.scalatest.{FlatSpec, Matchers}

class FileReaderSpec extends FlatSpec with Matchers {

    "apply" should "return an instance of BufferedSourceAdapter" in {

        FileReader.apply("aNiceFile.txt").getClass should equal (new BufferedSourceAdapter("aNiceFile.txt").getClass)

    }

}
