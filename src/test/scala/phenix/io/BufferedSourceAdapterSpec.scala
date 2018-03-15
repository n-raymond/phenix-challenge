package phenix.io

import org.scalamock.scalatest.MockFactory
import org.scalatest._

class BufferedSourceAdapterSpec extends FlatSpec with Matchers with MockFactory {

    "getFileLines" should "succeed to retrieve a file and read his lines" in {
        val result = new BufferedSourceAdapter("src/test/resources/io-handler-spec-0.txt").readLines.toList
        val expected = List("a", "b", "c")
        result should equal (expected)
    }

    /* TODO: Should be testeed in DatedDataFile */
    /*it should "refresh the open variable" in {
        val reader = new BufferedSourceAdapter("src/test/resources/io-handler-spec-0.txt")
        reader.readLines
        reader.isOpened should equal (true)
    }*/

    /* TODO: Should be tested in DatedDataFile */
    /*it should "throw an IllegalStateException if used a second time" in {
        val reader = new BufferedSourceAdapter("src/test/resources/io-handler-spec-0.txt")
        reader.readLines
        an [IllegalStateException] should be thrownBy reader.readLines
    }*/

    /* TODO: Should be tested in DatedDataFile */
    /*it should "throw an FileNotFoundException if the file does not exist" in {
        val reader = new BufferedSourceAdapter("src/test/resources/io-handler-spec-an-inexistant-file.txt")
        an [FileNotFoundException] should be thrownBy reader.readLines
    }*/


}
