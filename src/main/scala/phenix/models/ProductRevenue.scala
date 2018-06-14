package phenix.models

import phenix.models.exceptions.DeserializationException

/**
  * Associates a product to its price.
  */
case class ProductRevenue(product: Int, price: Double) {

    override def toString: String = s"$product|$price"
}


object ProductRevenue {

    /**
      * Builds a ProductRevenue by deserializing it from a string.
      * @param productRevenue a string representing a product value with the following format:
      *                    5|129
      * @return a product quantity computed from the given serialized transaction
      */
    @throws(classOf[DeserializationException])
    def apply(productRevenue: String): ProductRevenue = {
        try {
            val parsed = productRevenue.split('|')
            ProductRevenue(parsed(0).toInt, parsed(1).toDouble)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(productRevenue, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(productRevenue, e)
        }
    }
}