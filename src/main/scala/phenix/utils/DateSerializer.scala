package phenix.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
  * Adds functionalities to serialize LocalDates
  */
trait DateSerializer {


    /**
      * A DateFormatter used to generate the postfix part of the filename
      */
    private val fileNameDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    /**
      * Converts a LocalDate into a string
      * @param date The date to convert
      * @return A string in format "yyyyMMdd"
      */
    def stringOfDate(date: LocalDate) = date.format(fileNameDateFormatter)

}
