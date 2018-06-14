package phenix.dataFiles.general

import java.time.LocalDate


/**
  * Represents a file containing a certain type of data.
  * This trait provide help to store a file into a specific place
  * on the file system and to write a specific type of data in it.
  */
trait DataFile[Data] {

    /**
      * Represents the file location on the file system.
      */
    def fileLocation : String

    /**
      * Computes the file name.
      * @return The file name.
      */
    def fileName: String

    /**
      * Parses the given line of the file to compute some Data.
      * @param serializedData The serialized form of the Data
      * @return an instance of Data
      */
    def deserializeData(serializedData: String) : Data

    /**
      * Serialize the given data to convert into a string.
      * @param data The data to serialize
      * @return a string representing the serialized Data
      */
    def serializeData(data: Data) : String

    /**
      *
      * @return the file date.
      */
    def date: LocalDate
}
