package phenix.utils

import scala.util.{Failure, Success, Try}

/**
  * Gives utilities to filter only successful values
  * from a Iterable of Try.
  */
trait SuccessFilter {

    /**
      * Filters the given Try values by keeping only the one who succeeded.
      *
      * @param tries An iterable of the Try values
      * @tparam T The type of the value
      * @return An iterable of the succeeded values.
      */
    def filterSuccessValues[T](tries: Iterable[Try[T]]): Iterable[T] = {
        tries filter {
            case Success(_) => true
            case Failure(_) => false
        } map {
            case Success(transaction) => transaction
            case Failure(e) => throw new IllegalStateException("Can't be a Failure by filter precondition", e)
        }
    }

}
