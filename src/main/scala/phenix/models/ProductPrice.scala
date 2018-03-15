package phenix.models

import phenix.models.exceptions.DeserializationException

/**
  * Associates a product to a price.
  */
case class ProductPrice(product: Int, price: Int) extends {

    override def toString: String = s"$product|$price"

}


object ProductPrice {

    /**
      * Builds a ProductValue by deserializing it from a string.
      * @param productPrice a string representing a product quantity with the following format:
      *                    5|129
      * @return a product quantity computed from the given serialized transaction
      */
    @throws(classOf[DeserializationException])
    def apply(productPrice: String): ProductPrice = {
        try {
            val parsed = productPrice.split('|')
            ProductPrice(parsed(0).toInt, parsed(1).toInt)
        } catch {
            case e: NumberFormatException => throw new DeserializationException(productPrice, e)
            case e: IndexOutOfBoundsException=> throw new DeserializationException(productPrice, e)
        }
    }
}



