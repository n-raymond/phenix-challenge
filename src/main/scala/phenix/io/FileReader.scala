package phenix.io

/**
  * Handles File reading operations.
  */
trait FileReader extends AutoCloseable {

    /**
      * Return an iterator containing each line of the current file as String.
      * @return The file content as a string iterator
      */
    def getFileLines : Iterator[String]

    /**
      * Close file resources
      */
    override def close(): Unit

}

object FileReader {

    /**
      * A factory to get the default FileReader.
      */
    def apply(fileName: String) : FileReader = new FileReaderWithBufferedSource(fileName)
}

