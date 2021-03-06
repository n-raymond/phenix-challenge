package phenix.dataFiles.general

import java.time.LocalDate

import com.typesafe.config.ConfigFactory
import phenix.io.IOService
import phenix.utils.DateSerializer


/**
  * A dated file containing a certain type of Data.
  * This partial implementation gives a convenient way to compute
  * the file name with a date in postfix.
  *
  * @param date           The date of the file
  */
abstract class DataFileImpl[Data](
    val date: LocalDate,
    val ioService: IOService
) extends DataFile[Data]
    with DateSerializer {

    /**
     * Computes the prefix of the fileName. The file's date will be appended
     * to this prefix to compute the the full file name.
     * @return The file name prefix
     * @return the main part of the file name from using the data's type name
     */
    protected def fileNamePrefix : String

    /** @inheritdoc */
    override def fileName : String = {
        s"$fileLocation/${fileNamePrefix}_${stringOfDate(date)}.data"
    }

}

object DataFileImpl {

    /**
      * The application configuration
      */
    private val conf = ConfigFactory.load()

    /**
     * Adds the location of files that are initial data.
     */
    trait LocatedInDataImpl[Data] extends DataFileImpl[Data] {

        /** @inheritdoc */
        override def fileLocation : String = s"${ioService.rootPath}/${DataFileImpl.conf.getString("paths.data")}"
    }

    /**
     * Adds the location of files that are produced by this software.
    */
    trait LocatedInResult[Data] extends DataFileImpl[Data] {

        /** @inheritdoc */
        override def fileLocation : String = s"${ioService.rootPath}/${DataFileImpl.conf.getString("paths.result")}"
    }


}


