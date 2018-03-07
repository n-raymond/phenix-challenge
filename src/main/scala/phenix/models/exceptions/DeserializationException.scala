package phenix.models.exceptions

/**
  * An exception which must be thrown when a model failed to be deserialized.
  * @param data The string representing model that failed to be deserialized
  * @param cause The throwable which caused the failure.
  */
case class DeserializationException(data: String, cause: Throwable = None.orNull)
    extends IllegalArgumentException(s"The following data could not be parsed : ${data}", cause)
