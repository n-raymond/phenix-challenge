package example

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util.UUID

/**
  * A transaction with of id, that happened at the given datetime, in the given shop,
  * for a given product and at a given quantity.
  */
case class Transaction(txId: Int, datetime: LocalDate, shop: UUID, product: Int, quantity: Int)

object Transaction {

    val formater = DateTimeFormatter.ofPattern("yyyyMMdd")

    /**
      * Builds a Transaction by deserializing it from a string.
      * @param transaction a string representing a transaction with the following format:
      *                    5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3
      * @return a Transaction computed from the given serialized transaction
      */
    def apply(transaction: String) : Transaction = {
        try {
            val parsed = transaction.split('|')
            Transaction(parsed(0).toInt, deserializeDate(parsed(1)), UUID.fromString(parsed(2)), parsed(3).toInt, parsed(4).toInt)
        } catch {
            case e: DateTimeParseException => throw new TransactionDeserializationException(transaction, e)
            case e: NumberFormatException => throw new TransactionDeserializationException(transaction, e)
            case e: IllegalArgumentException => throw new TransactionDeserializationException(transaction, e)
            case e: IndexOutOfBoundsException=> throw new TransactionDeserializationException(transaction, e)
        }
    }

    /**
      * Deserializes a date.
      * @param date A string containing a date in ISO-8601 with the following format: 20170514T004132+0100
      * @return A LocalDate that matches the given serialize date
      * @throws DateTimeParseException if the LocalDate could not be deserialized.
      */
    private def deserializeDate(date: String) : LocalDate = LocalDate.parse(date.substring(0, 8), formater)
}


case class TransactionDeserializationException(transaction: String, cause: Throwable = None.orNull)
    extends Exception(s"The following transaction could not be parsed : ${transaction}", cause)