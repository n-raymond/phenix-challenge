package phenix.io

import phenix.io.reader.{BufferedSourceAdapter, FileReader}
import phenix.io.writer.{FileWriter, PrintWriterAdapter}

/**
  * A service containing several factories to produce IO adapters.
  */
trait IOService {

    /**
      * A factory to get the default FileReader.
      */
    def getFileReader(fileName: String) : FileReader

    /**
      * A factory to get the default FileWriter.
      */
    def getFileWriter(fileName: String) : FileWriter

}
