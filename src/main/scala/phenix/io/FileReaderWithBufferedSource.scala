package phenix.io

import scala.io.Source

/**
  * Main File Reader and writter implementation.
  * This singleton offers a way to do read operations by using
  * the scala.io.Sources.
  */
class FileReaderWithBufferedSource(val fileName: String) extends FileReader with AutoCloseable {

    /**
      * The BufferedSource used to read the file.
      */
    private lazy val source = Source.fromFile(fileName)

    /** @inheritdoc */
    override def getFileLines : Iterator[String] = source.getLines

    /** @inheritdoc */
    override def close(): Unit = source.close()

}
