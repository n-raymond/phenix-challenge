package phenix.io

import java.io.IOException

import phenix.io.reader.FileReader
import phenix.io.writer.FileWriter

import scala.util.matching.Regex

/**
  * A service containing several factories to produce IO adapters
  * and some functionalities to handle filesystem.
  */
trait IOService {

    /* IO Adapters */

    /**
      * A factory to get the default FileReader.
      */
    def getFileReader(fileName: String) : FileReader

    /**
      * A factory to get the default FileWriter.
      */
    def getFileWriter(fileName: String) : FileWriter



    /* Filesystem helpers */

    /**
      * Checks if the parent path of the given file exists and if not, create
      * all the corresponding folder structure.
      */
    @throws[IOException]
    def createParentPathIfNotExist(path: String): Unit

    /**
      *
      * Try to find some files on filesystem in a certain directory
      * that match the given regex.
      * The search is not recursive.
      * @param directory The directory were the files must be found
      * @param regex The regex used to find the files by name.
      * @return An Iterator of found file's names
      */
    def findFilesNameByRegex(directory: String, regex: Regex) : Iterator[String]

}
