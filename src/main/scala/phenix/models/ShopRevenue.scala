package phenix.models

import java.util.UUID

import phenix.models.exceptions.DeserializationException


/**
  * Represents a revenue of a product that was sold (someday) in
  * a specific shop.
  */
case class ShopRevenue(shop: UUID, revenue: Double) {

    override def toString: String = s"$shop|$revenue"

}

object ShopRevenue {

    /**
      * Builds a ProductRevenue by deserializing it from a string.
      * @param productQty a string representing a product quantity with the following format:
      *                    8e588f2f-d19e-436c-952f-1cdd9f0b12b0|129.7
      * @return a product quantity computed from the given serialized transaction
      */
    @throws(classOf[DeserializationException])
    def apply(productQty: String): ShopRevenue = {
        try {
            val parsed = productQty.split('|')
            ShopRevenue(UUID.fromString(parsed(0)), parsed(1).toDouble)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(productQty, e)
            case e: IllegalArgumentException => throw new DeserializationException(productQty, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(productQty, e)
        }
    }
}

