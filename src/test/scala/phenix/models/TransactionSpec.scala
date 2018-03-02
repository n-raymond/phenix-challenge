package phenix.models

import java.time.LocalDate
import java.util.UUID

import org.scalatest._
import phenix.models.exceptions.DeserializationException

class TransactionSpec extends FlatSpec with Matchers {

    val deserialized = Transaction(
        5,
        LocalDate.of(2017, 5, 14),
        UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"),
        266,
        3
    )

    val serialized = "5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3"

    "Transaction.apply" should "succeed to parse a valid entry" in {
        Transaction(serialized) should equal (deserialized)
    }

    "toString" should "return a valid serialized Transaction" in {
        //Since we only used the date, time is set to zero by default
        val expected = "5|20170514T000000+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3"
        deserialized.toString should equal (expected)
    }

    it should "throw an exception if the UUID format is not good" in {
        an [DeserializationException] should be thrownBy Transaction("5|20170514T004132+0100|8e5819e-436c-952f-1cdd9f0b12b0|266|3")
    }

    it should "throw an exception if the date is not serializable" in {
        an [DeserializationException] should be thrownBy Transaction("5|201aefi70514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3")
    }

    it should "throw an exception if a number is not formatable" in {
        an [DeserializationException] should be thrownBy Transaction("5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|26azdaz6|3")
    }

    it should "throw an exception if a field is missing" in {
        an [DeserializationException] should be thrownBy Transaction("5|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3")
    }

}
