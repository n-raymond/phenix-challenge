package phenix.utils

trait StrictlyNaturalIntDeserializer {

    def strictlyPositiveDeserialization(s: String) : Int = {
        val i = s.toInt
        if(i <= 0) {
            throw new IllegalArgumentException(s"The following int is not strictly positive : $i")
        }
        i
    }

}
