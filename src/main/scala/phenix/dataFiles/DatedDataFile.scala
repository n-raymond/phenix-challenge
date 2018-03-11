package phenix.dataFiles

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import phenix.io.{FileReader, FileWriter}
import scala.util.Try


/**
  * A helper to manage files containing a certain type of Data on each line.
  * A DatedDataFile binds to the specific type of is Data a convenient way to compute
  * his file name in order to retrieve it with a date. The date of the file appears
  * in postfix in this name.
  * @param date The date of the DatedDataFile
  */
abstract class DatedDataFile(val date: LocalDate) {

    /**
      * Each line of the file will contain a serialize version of the data.
      */
    type Data

    /**
      * Computes the prefix of the fileName. The file's date will be appended
      * to this prefix to compute the the full file name.
      * @return The file name prefix
      */
    protected def fileNamePrefix : String

    /**
      * Parses the given line of the file to compute some Data.
      * @param serializedData The serialized form of the Data
      * @return an instance of Data
      */
    protected def parseData(serializedData: String) : Data

    /**
      * Computes the file name.
      * @return The file name.
      */
    def fileName = s"${fileNamePrefix}_${date.format(DatedDataFile.fileNameDateFormatter)}.data"

}

object DatedDataFile {

    /**
      * A DateFormatter used to generate the postfix part of the filename
      */
    private val fileNameDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")


    /**
      * Adds helping functionalities to read, parse and retrieve data
      * from a DatedDataFile in a lazy way.
      * If the DatedDataFile extends this trait, it will hold resources
      * as long as it is not closed.
      */
    trait Readable extends DatedDataFile with AutoCloseable {

        /**
          * The file reader that will be used to read some file.
          */
        var fileReader : FileReader = FileReader(fileName)

        /**
          * Closes resources related to the file.
          */
        override def close(): Unit = fileReader.close()

        /**
          * Gives a Stream containing all the Data inputs of the file.
          * @return The content of the file as a stream of Data
          */
        def getContent : Stream[Try[Data]] = {
            fileReader.getFileLines.toStream map { line => Try(parseData(line)) }
        }

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
        def getChunks(chunkSize: Int) : Stream[(Int, Chunk)] = {
            def partitioning(groups: Iterator[Chunk], offset: Int = 0): Stream[(Int, Chunk)] = {
                if(groups.hasNext) {
                    val group = groups.next
                    (offset, group) #:: partitioning(groups, offset + 1)
                }
                else {
                    Stream.empty
                }
            }
            partitioning(getContent.grouped(chunkSize))
        }
    }


    /**
      * Adds helping functionalities to write data on the DatedDataFile.
      * If the DatedDataFile extends this trait, it will hold resources
      * as long as it is not closed.
      */
    trait Writable extends DatedDataFile with AutoCloseable {

        /**
          * The file writer that will be used to write on some file.
          */
        var fileWriter : FileWriter = FileWriter(fileName)

        /**
          * Closes resources related to the file.
          */
        override def close(): Unit = fileWriter.close()

        /**
          * Write each element of the data on each line of
          * the current file.
          * @param lines A stream containing each data element to write on the file lines
          */
        def writeData(lines: Stream[Data]) : Unit = {
            fileWriter.writeLines(lines map (_.toString))
        }

    }

}


