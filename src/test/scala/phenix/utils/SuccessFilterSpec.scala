package phenix.utils

import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

class SuccessFilterSpec extends FlatSpec with Matchers {

    class A extends SuccessFilter

    "filterSuccessValues" should "keep only successful values without throwing an IllegalStateException" in {
        val initialValues = Iterable(
            Success(3),
            Success(10),
            Success(11),
            Failure(new Exception("Mouahahah ! You failed !")),
            Success(385),
            Success(75),
            Failure(new Exception("Mouahahah ! You failed !")),
            Success(76)
        )

        val result = new A().filterSuccessValues(initialValues)

        val expected = Iterable(3, 10, 11, 385, 75, 76)

        result should equal (expected)
    }

}
