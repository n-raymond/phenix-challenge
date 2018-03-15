package phenix.dataProcessors

import org.scalatest.{FlatSpec, Matchers}

class ProductQuantityAggregeableSpec extends FlatSpec with Matchers {

    "apply" should "return an instance of MapReduceProductQuantityAggregator" in {

        ProductQuantityAggregeable.apply should equal (MapReduceProductQuantityAggregator)

    }

}
