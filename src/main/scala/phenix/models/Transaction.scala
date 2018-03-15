package phenix.models

import java.util.UUID

import phenix.models.exceptions.DeserializationException

/**
  * A transaction that has a certain id and happened at the given datetime, in the given shop,
  * for a given product sold at a given quantity.
  */
case class Transaction(shop: UUID, product: Int, quantity: Int) {

    override def toString: String = s"0|20000101T000000+01000|$shop|$product|$quantity"

}

object Transaction {

    /**
      * Builds a Transaction by deserializing it from a string.
      * @param transaction a string representing a transaction with the following format:
      *                    5|20170514T004132+0100|8e588f2f-d19e-436c-952f-1cdd9f0b12b0|266|3
      * @return a Transaction computed from the given serialized transaction
      */
    @throws(classOf[DeserializationException])
    def apply(transaction: String) : Transaction = {
        try {
            val parsed = transaction.split('|')
            /* We are ignoring the transaction id and the date since it not be used... */
            Transaction(UUID.fromString(parsed(2)), parsed(3).toInt, parsed(4).toInt)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(transaction, e)
            case e: IllegalArgumentException => throw new DeserializationException(transaction, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(transaction, e)
        }
    }
}
