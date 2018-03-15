package phenix.dataFiles

import java.io.{FileNotFoundException, IOException}

/**
  * A file containing Data that can be written.
  */
trait WritableDataFile extends DataFile {

    /**
      * Write each element of the data on each line of the current file.
      * @param lines An iterable containing each data element to write on the file lines
      */
    @throws[IOException]
    @throws[FileNotFoundException]
    @throws[IllegalStateException]
    def writeData(lines: Iterable[Data]) : Unit

}
