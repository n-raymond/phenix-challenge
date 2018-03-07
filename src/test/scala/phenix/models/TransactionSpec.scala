package phenix.models

import java.time.LocalDate
import java.util.UUID

import org.scalatest._
import phenix.models.exceptions.DeserializationException

class TransactionSpec extends FlatSpec with Matchers {

    val deserialized = Transaction(UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"), 266, 3)

    val serialized = "5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3"

    "Transaction.apply" should "succeed to parse a valid entry" in {
        Transaction(serialized) should equal (deserialized)
    }

    it should "throw an exception if the UUID format is not good" in {
        a [DeserializationException] should be thrownBy Transaction("5|20170514T004132+0100|8e5819e-436c-952f-1cdd9f0b12b0|266|3")
    }

    it should "throw an exception if a number is not formatable" in {
        a [DeserializationException] should be thrownBy Transaction("5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|26azdaz6|3")
    }

    it should "throw an exception if a field is missing" in {
        a [DeserializationException] should be thrownBy Transaction("5|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3")
    }

}
