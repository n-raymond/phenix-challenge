package phenix.io

import java.nio.file.{Files, Paths}

import phenix.io.reader.{BufferedSourceAdapter, FileReader}
import phenix.io.writer.{FileWriter, PrintWriterAdapter}

import scala.collection.JavaConverters._
import scala.util.matching.Regex

/**
  * The default implementation of IOService.
  */
class IOServiceImpl extends IOService {

    /** @inheritdoc */
    override def getFileReader(fileName: String) : FileReader = new BufferedSourceAdapter(fileName)

    /** @inheritdoc */
    override def getFileWriter(fileName: String) : FileWriter = new PrintWriterAdapter(fileName)

    /** @inheritdoc */
    override def createParentPathIfNotExist(path: String): Unit = {
        val parent = Paths.get(path).getParent

        if (!Files.exists(parent)) {
            Files.createDirectories(parent)
        }
    }

    /** @inheritdoc */
    override def findFilesNameByRegex(directory: String, regex: Regex): Iterator[String] = {
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
