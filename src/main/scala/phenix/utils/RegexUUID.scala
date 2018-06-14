package phenix.utils

/**
  * Provides a string that can be used as a regex to
  * find a UUID.
  */
trait RegexUUID {

    val regexUUID = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"

}
