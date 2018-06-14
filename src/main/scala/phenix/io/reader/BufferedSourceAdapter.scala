package phenix.io.reader

import scala.io.Source

/**
  * An adapter used to read in a file using a scala.io.Sources.
  * @param fileName The file name
  */
class BufferedSourceAdapter(val fileName: String) extends FileReader with AutoCloseable {

    /**
      * The BufferedSource used to read the file.
      */
    private lazy val source : Source = Source.fromFile(fileName)

    /** @inheritdoc */
    override def readLines : Iterator[String] = source.getLines

    /** @inheritdoc */
    override def close(): Unit = source.close()

}
