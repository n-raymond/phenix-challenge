package phenix.utils

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class ResourceCloseableSpec extends FlatSpec with Matchers with MockFactory with ResourceCloseable {

    class CloseableInt(val value: Int) extends AutoCloseable {
        override def close(): Unit = {}
    }

    "tryWith" should "close the given Closeable" in {
        val closeable = mock[CloseableInt]
        (closeable.close _).expects()
        tryWith(closeable) (_.value)
    }
}