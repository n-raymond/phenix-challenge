package phenix.io

import phenix.io.reader.{BufferedSourceAdapter, FileReader}
import phenix.io.writer.{FileWriter, PrintWriterAdapter}

class IOServiceImpl extends IOService {

    /** @inheritdoc */
    override def getFileReader(fileName: String) : FileReader = new BufferedSourceAdapter(fileName)

    /** @inheritdoc */
    override def getFileWriter(fileName: String) : FileWriter = new PrintWriterAdapter(fileName)

}
