package phenix.models

import phenix.models.exceptions.DeserializationException

/**
  * Associates a product id to a quantity.
  */
case class ProductQuantity(product: Int, quantity: Int) {

    override def toString: String = s"$product|$quantity"

}


object ProductQuantity {

    /**
      * Builds a ProductQuantity by deserializing it from a string.
      * @param productQuantity a string representing a product value with the following format:
      *                    5|129
      * @return a product quantity computed from the given serialized transaction
      */
    @throws(classOf[DeserializationException])
    def apply(productQuantity: String): ProductQuantity = {
        try {
            val parsed = productQuantity.split('|')
            ProductQuantity(parsed(0).toInt, parsed(1).toInt)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(productQuantity, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(productQuantity, e)
        }
    }
}



