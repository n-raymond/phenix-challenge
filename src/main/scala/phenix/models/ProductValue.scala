package phenix.models

import phenix.models.exceptions.DeserializationException

/**
  * Associates a product to a value.
  * The value can be a price, a number, etc...
  */
case class ProductValue(product: Int, value: Int) {

    override def toString: String = s"$product|$value"

}


object ProductValue {

    /**
      * Builds a ProductValue by deserializing it from a string.
      * @param productValue a string representing a product value with the following format:
      *                    5|129
      * @return a product quantity computed from the given serialized transaction
      */
    @throws(classOf[DeserializationException])
    def apply(productValue: String): ProductValue = {
        try {
            val parsed = productValue.split('|')
            ProductValue(parsed(0).toInt, parsed(1).toInt)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(productValue, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(productValue, e)
        }
    }
}



