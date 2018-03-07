package phenix.models

import phenix.models.exceptions.DeserializationException

/**
  * Associates a product to a quantity that was sold.
  */
case class ProductQuantity(product: Int, quantity: Int) {

    override def toString: String = s"${product}|${quantity}"

}

object ProductQuantity {


    /**
      * A factory to build a ProductQuantity by deserializing it from a string.
      * @param productQty a string representing a product quantity with the following format:
      *                    5|129
      * @return a product quantity computed from the given serialized transaction
      */
    def apply(productQty: String): ProductQuantity = {
        try {
            val parsed = productQty.split('|')
            ProductQuantity(parsed(0).toInt, parsed(1).toInt)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(productQty, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(productQty, e)
        }
    }
}

