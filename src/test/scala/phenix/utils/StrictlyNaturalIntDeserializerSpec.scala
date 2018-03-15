package phenix.utils

import org.scalatest.{FlatSpec, Matchers}

class StrictlyNaturalIntDeserializerSpec extends FlatSpec with Matchers with StrictlyNaturalIntDeserializer {

    "deserialize" should "succed to deserialize a strictly positive" in {
        strictlyPositiveDeserialization("2") should equal (2)
    }

    it should "throw an IllegalArgumentException if 0 or < 0" in {
        a [IllegalArgumentException] should be thrownBy strictlyPositiveDeserialization("-2")
        a [IllegalArgumentException] should be thrownBy strictlyPositiveDeserialization("0")
    }

}
