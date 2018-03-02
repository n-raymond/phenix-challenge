package phenix.io

import java.io.{FileWriter => FW, BufferedWriter, PrintWriter}



class FileWriterWithPrintWriter(val fileName: String) extends FileWriter with AutoCloseable {

    private lazy val fileWriter = new FW(fileName, true)
    private lazy val bufferedWriter = new BufferedWriter(fileWriter)
    private lazy val printWriter = new PrintWriter(bufferedWriter)

    /** @inheritdoc */
    override def writeLines(lines: Iterable[String]): Unit = {
        lines foreach (printWriter.println(_))
    }

    /** @inheritdoc */
    override def close(): Unit = {
        printWriter.close()
        bufferedWriter.close()
        fileWriter.close()
    }

}
