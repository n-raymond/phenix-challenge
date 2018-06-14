package phenix.io.path

import java.nio.file.{Files, Paths}

import scala.collection.JavaConverters._
import scala.util.matching.Regex

/**
  * Adds functionalities to find some files on filesystem.
  */
trait FileFinder {

    def findFilesNameByRegex(directory: String, regex: Regex) : Iterator[String] = {
        val files = Files.walk(Paths.get(directory)).iterator().asScala
        val names = files map {
            _.getFileName.toString
        }
        names.filter { fileName =>
            regex.findFirstMatchIn(fileName) match {
                case Some(_) => true
                case None => false
            }
        }
    }

}
