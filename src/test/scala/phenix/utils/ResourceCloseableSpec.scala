package phenix.utils

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class ResourceCloseableSpec extends FlatSpec with Matchers with MockFactory with ResourceCloseable {


    "tryWith" should "close the given Closeable" in {
        val closeable = mock[AutoCloseable]
        (closeable.close _).expects()
        tryWith(closeable) (println)
    }
}