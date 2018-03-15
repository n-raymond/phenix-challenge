
package phenix.io

import java.io.IOException

/**
  * Handles File writing operations.
  */
trait FileWriter extends AutoCloseable {

    /**
      * Write all the string contained by the given string iterator
      * as a new line in the file. If the file does not exist, it
      * should be created.
      * @return The file content as a string iterator
      */
    @throws[IOException]
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
    def apply(fileName: String) : FileWriter = new PrintWriterAdapter(fileName)
}

