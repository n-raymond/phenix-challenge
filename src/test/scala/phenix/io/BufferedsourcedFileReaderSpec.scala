package phenix.io

import org.scalatest._

class BufferedsourcedFileReaderSpec extends FlatSpec with Matchers {

    "getFileLines" should "succeed to retrieve a file and read his lines" in {
        val result = new FileReaderWithBufferedSource("src/test/resources/io-handler-spec-0.txt").getFileLines.toList
        val expected = List("a", "b", "c")
        result should equal (expected)
    }

}
