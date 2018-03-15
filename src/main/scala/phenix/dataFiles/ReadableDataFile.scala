package phenix.dataFiles

import java.io.{FileNotFoundException, IOException}

import scala.util.Try


/**
  * A file containing data that can be read.
  */
trait ReadableDataFile extends DataFile {

    /**
      * Gives a Stream containing all the Data inputs of the file.
      * @return The content of the file as a stream of Data
      */
    @throws[IOException]
    @throws[FileNotFoundException]
    @throws[IllegalStateException]
    def getContent : Stream[Try[Data]]

    /**
      * A chunk represents a sub-part of a file
      */
    type Chunk = Stream[Try[Data]]

    /**
      * Divide the current file into a stream of his different parts (a chunk) of size chunkSize.
      * Each part is associated with his own identifier.
      * @param chunkSize The number of element in each chunks
      * @return A stream of the different parts of the file
      */
    @throws[IOException]
    @throws[FileNotFoundException]
    @throws[IllegalStateException]
    def getChunks(chunkSize: Int) : Stream[(Int, Chunk)]

}