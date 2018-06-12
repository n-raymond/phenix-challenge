package phenix.models

import org.scalatest._
import phenix.models.exceptions.DeserializationException

class ProductValueSpec extends FlatSpec with Matchers {

    val deserialized = ProductValue(5, 266)

    val serialized = "5|266"

    "Transaction.apply" should "succeed to parse a valid entry" in {
        ProductValue(serialized) should equal (deserialized)
    }

    "toString" should "return a valid serialized Transaction" in {
        deserialized.toString should equal (serialized)
    }

    it should "throw an exception if a number is not formatable" in {
        a [DeserializationException] should be thrownBy ProductValue("5|qzefzef")
    }

    it should "throw an exception if a field is missing" in {
        a [DeserializationException] should be thrownBy ProductValue("5|")
    }

}
