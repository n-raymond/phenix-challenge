package phenix.dataFiles.impl

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.typesafe.config.ConfigFactory
import phenix.dataFiles.DataFile
import phenix.utils.CamelCaseToUnderscore


/**
  * A dated file containing a certain type of Data.
  * This partial implementation gives a convenient way to compute
  * the file name with a date in postfix.
  *
  * @param date           The date of the file
  */
abstract class DataFileImpl[Data](
    val date: LocalDate
) extends DataFile[Data] with CamelCaseToUnderscore {

    /**
     * Computes the prefix of the fileName. The file's date will be appended
     * to this prefix to compute the the full file name.
     * @return The file name prefix
     * @return the main part of the file name from using the data's type name
     */
    protected def fileNamePrefix : String

    /** @inheritdoc */
    override def fileName : String = {
        s"$fileLocation/${fileNamePrefix}_${date.format(DataFileImpl.fileNameDateFormatter)}.data"
    }

}

object DataFileImpl {

    /**
      * The application configuration
      */
    private val conf = ConfigFactory.load()

    /**
      * A DateFormatter used to generate the postfix part of the filename
      */
    private val fileNameDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    /**
     * Adds the location of files that are initial data.
     */
    trait LocatedInDataImpl[Data] extends DataFileImpl[Data] {

        /** @inheritdoc */
        override def fileLocation : String = DataFileImpl.conf.getString("paths.data")
    }

    /**
     * Adds the location of files that are produced by this software.
    */
    trait LocatedInResult[Data] extends DataFileImpl[Data] {

        /** @inheritdoc */
        override def fileLocation : String = DataFileImpl.conf.getString("paths.result")
    }


}


