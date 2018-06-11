package phenix.dataFiles

import java.io.{FileNotFoundException, IOException}

import phenix.utils.Openable

/**
  * A file containing Data that can be written.
  */
trait WritableDataFile[Data] extends DataFile[Data] with Openable {

    /**
      * Write each element of the data on each line of the current file.
      * @param lines An iterable containing each data element to write on the file lines
      */
    @throws[IOException]
    @throws[FileNotFoundException]
    @throws[IllegalStateException]
    def writeData(lines: Iterable[Data]) : Unit

}
