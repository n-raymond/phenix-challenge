package phenix.io.reader

import scala.io.Source

/**
  * Main File Reader and writer implementation.
  * This singleton offers a way to do read operations by using
  * the scala.io.Sources.
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
