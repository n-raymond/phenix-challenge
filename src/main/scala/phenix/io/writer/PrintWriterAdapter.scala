package phenix.io.writer

import java.io.{BufferedWriter, PrintWriter, FileWriter => FW}


/**
  * An adapter used to write in a file using a java.io.PrintWriter.
  * @param fileName The file name
  */
class PrintWriterAdapter(val fileName: String) extends FileWriter with AutoCloseable {

    private lazy val fileWriter = new FW(fileName, false)
    private lazy val bufferedWriter = new BufferedWriter(fileWriter)
    private lazy val printWriter = new PrintWriter(bufferedWriter)

    /** @inheritdoc */
    override def writeLines(lines: Iterable[String]): Unit = {
        lines foreach printWriter.println
    }

    /** @inheritdoc */
    override def close(): Unit = {
        printWriter.close()
        bufferedWriter.close()
        fileWriter.close()
    }

}
