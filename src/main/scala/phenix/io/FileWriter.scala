
package phenix.io

/**
  * Handles File writing operations.
  */
trait FileWriter extends AutoCloseable {

    /**
      * Write all the string contained by the given string iterator
      * as a new line in the file.
      * @return The file content as a string iterator
      */
    def writeLines(lines : Iterable[String]): Unit

    /**
      * Close file resources
      */
    override def close(): Unit

}


object FileWriter {

    /**
      * A factory to get the default FileWriter.
      */
    def apply(fileName: String) : FileWriter = new FileWriterWithPrintWriter(fileName)
}

