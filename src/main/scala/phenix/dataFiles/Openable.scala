package phenix.dataFiles

/**
  * A resource that can be opened or closed.
  */
trait Openable extends AutoCloseable {

    /**
      * Returns the fact the reader is open.
      * @return true if the reader is open, false if not.
      */
    def isOpen: Boolean

    /**
      * Closes resources related to the file.
      */
    override def close(): Unit

}