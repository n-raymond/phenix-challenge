package example

import java.time.{LocalDate}
import java.util.UUID

import org.scalatest._

class TransactionSpec extends FlatSpec with Matchers {
    "Transaction.apply" should "succed to parse a valid entry" in {
        val result = Transaction("5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3")
        val expected = Transaction(
            5,
            LocalDate.of(2017, 5, 14),
            UUID.fromString("8e588f2f-d19e-436c-952f-1cdd9f0b12b0"),
            266,
            3
        )
        result shouldEqual expected
    }

    "it" should "throw an exception if the UUID format is not good" in {
        an [TransactionDeserializationException] should be thrownBy Transaction("5|20170514T004132+0100|8e5819e-436c-952f-1cdd9f0b12b0|266|3")
    }

    "it" should "throw an exception if the date is not serializable" in {
        an [TransactionDeserializationException] should be thrownBy Transaction("5|201aefi70514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3")
    }

    "it" should "throw an exception if a number is not formatable" in {
        an [TransactionDeserializationException] should be thrownBy Transaction("5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|26azdaz6|3")
    }

    "it" should "throw an exception if a field is missing" in {
        an [TransactionDeserializationException] should be thrownBy Transaction("5|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3")
    }
}
