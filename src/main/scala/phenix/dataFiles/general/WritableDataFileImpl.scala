package phenix.dataFiles.general

import phenix.io.path.PathCreator
import phenix.io.writer.FileWriter

/**
  * Adds helping functionalities to write data on the DatedDataFile.
  * If the DatedDataFile extends this trait, it will hold resources
  * as long as it is not closed. Those IO resources will be requested
  * only when reading operations like getContent or getChunks are
  * called.
  */
trait WritableDataFileImpl[Data] extends DataFileImpl[Data] with WritableDataFile[Data] {

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
        PathCreator.apply.createParentPathIfNotExist(fileName)
        fileWriter.writeLines(lines map (_.toString))
    }
}
