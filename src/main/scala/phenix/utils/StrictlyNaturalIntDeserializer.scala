package phenix.utils

/**
  * Gives utilities to parse the given string into an int and failing if
  * this int is not strictly positive
  */
trait StrictlyNaturalIntDeserializer {

    /**
      * Convert the given string into an int and throws an IllegalArgumentException
      * if this int is not strictly positive.
      * @param s The string to parse
      * @return The given int
      */
    @throws(classOf[IllegalArgumentException])
    def strictlyPositiveDeserialization(s: String) : Int = {
        val i = s.toInt
        if(i <= 0) {
            throw new IllegalArgumentException(s"The following int is not strictly positive : $i")
        }
        i
    }

}
