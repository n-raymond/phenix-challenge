package phenix.dataFiles.general

import phenix.io.reader.FileReader

import scala.util.Try

/**
  * Adds helping functionalities to read, parse and retrieve data
  * from a DatedDataFile in a lazy way.
  * If the DatedDataFile extends this trait, it will hold resources
  * as long as it is not closed. Those IO resources will be requested
  * only when reading operations like getContent or getChunks are
  * called.
  */
trait ReadableDataFileImpl[Data] extends DataFileImpl[Data] with ReadableDataFile[Data] {

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
