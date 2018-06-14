package phenix.models

import java.util.UUID

import phenix.models.exceptions.DeserializationException


/**
  * Associates a revenue to the shop in which a product was sold.
  */
case class ShopRevenue(shop: UUID, revenue: Double) {

    override def toString: String = s"$shop|$revenue"

}

object ShopRevenue {

    /**
      * Builds a ShopRevenue by deserializing it from a string.
      * @param shopRevenue a string representing a product quantity with the following format:
      *                    8e588f2f-d19e-436c-952f-1cdd9f0b12b0|129.7
      * @return a product quantity computed from the given serialized transaction
      */
    @throws(classOf[DeserializationException])
    def apply(shopRevenue: String): ShopRevenue = {
        try {
            val parsed = shopRevenue.split('|')
            ShopRevenue(UUID.fromString(parsed(0)), parsed(1).toDouble)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(shopRevenue, e)
            case e: IllegalArgumentException => throw new DeserializationException(shopRevenue, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(shopRevenue, e)
        }
    }
}

