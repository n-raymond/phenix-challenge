package phenix.dataFiles

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.typesafe.config.ConfigFactory
import phenix.io.{FileReader, FileWriter}

import scala.util.Try


/**
  * A dated file containing a certain type of Data.
  * This partial implementation gives a convenient way to compute
  * the file name with a date in postfix.
  * @param date The date of the file
  */
abstract class DatedDataFile(val date: LocalDate) extends DataFile {

    /**
      * Computes the prefix of the fileName. The file's date will be appended
      * to this prefix to compute the the full file name.
      * @return The file name prefix
      */
    protected def fileNamePrefix : String

    /** @inheritdoc */
    override def fileLocation : String = DatedDataFile.conf.getString("paths.data")

    /** @inheritdoc */
    override def fileName = s"$fileLocation/${fileNamePrefix}_${date.format(DatedDataFile.fileNameDateFormatter)}.data"

}

object DatedDataFile {

    /**
      * The application configuration
      */
    private val conf = ConfigFactory.load()

    /**
      * A DateFormatter used to generate the postfix part of the filename
      */
    private val fileNameDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    /**
      * Adds helping functionalities to read, parse and retrieve data
      * from a DatedDataFile in a lazy way.
      * If the DatedDataFile extends this trait, it will hold resources
      * as long as it is not closed. Those IO resources will be requested
      * only when reading operations like getContent or getChunks are
      * called.
      */
    trait Readable extends DatedDataFile with ReadableDataFile with Openable {

        /**
          * The file reader that will be used to read some file.
          */
        var fileReader : FileReader = FileReader(fileName)

        /**
          * Represents the state of reader resources.
          */
        private var open : Boolean = false



        /** @inheritdoc */
        override def isOpen: Boolean = open

        /** @inheritdoc */
        override def close(): Unit = fileReader.close()

        /** @inheritdoc */
        override def getContent : Stream[Try[Data]] = {
            if(open) {
                throw new IllegalStateException("The file could be read only one time per reader.")
            }
            open = true
            fileReader.readLines.toStream map { line => Try(deserializeData(line)) }
        }

        /** @inheritdoc */
        override def getChunks(chunkSize: Int) : Stream[(Int, Chunk)] = {
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
      * as long as it is not closed. Those IO resources will be requested
      * only when reading operations like getContent or getChunks are
      * called.
      */
    trait Writable extends DatedDataFile with WritableDataFile with Openable {

        /**
          * The file writer that will be used to write on some file.
          */
        var fileWriter : FileWriter = FileWriter(fileName)

        /**
          * Represents the state of reader resources.
          */
        private var open : Boolean = false



        /** @inheritdoc */
        override def isOpen: Boolean = open

        /** @inheritdoc */
        override def close(): Unit = fileWriter.close()


        /** @inheritdoc */
        override def writeData(lines: Iterable[Data]) : Unit = {
            if(open) {
                throw new IllegalStateException("The file could be written only one time per reader.")
            }
            open = true
            fileWriter.writeLines(lines map { data => serializeData(data) })
        }
    }

}


