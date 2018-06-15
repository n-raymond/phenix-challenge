package phenix.utils

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class ResourceCloseableSpec extends FlatSpec with Matchers with MockFactory with ResourceCloseable {


    "tryWith" should "close the given Closeable" in {
        val closeable = mock[AutoCloseable]
        closeable.close _ expects()
        tryWith(closeable) (println) (_=>())
    }

    it should "close the given Iterable[Closeable]" in {
        val closeable1 = mock[AutoCloseable]
        val closeable2 = mock[AutoCloseable]
        val closeable3 = mock[AutoCloseable]

        closeable1.close _ expects()
        closeable2.close _ expects()
        closeable3.close _ expects()

        tryWith(Iterable(closeable1, closeable2, closeable3)) (println) (_=>())
    }

}