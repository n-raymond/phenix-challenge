package phenix.models

import org.scalatest._
import phenix.models.exceptions.DeserializationException

class ProductQuantitySpec extends FlatSpec with Matchers {

    val deserialized = ProductQuantity(5, 266)

    val serialized = "5|266"

    "Transaction.apply" should "succeed to parse a valid entry" in {
        ProductQuantity(serialized) should equal (deserialized)
    }

    "toString" should "return a valid serialized Transaction" in {
        deserialized.toString should equal (serialized)
    }

    it should "throw an exception if a number is not formatable" in {
        a [DeserializationException] should be thrownBy ProductQuantity("5|qzefzef")
    }

    it should "throw an exception if a field is missing" in {
        a [DeserializationException] should be thrownBy ProductQuantity("5|")
    }

}
