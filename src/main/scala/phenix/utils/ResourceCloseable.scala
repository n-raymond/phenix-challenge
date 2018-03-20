package phenix.utils

/**
  * Used to add functionality of closing automatically
  * AutoCloseable objects.
  */
trait ResourceCloseable {

    /**
      * Applies the function f to a closeable element to get an element of type A
      * and automaticaly closes the closeable element after the operation.
      * @param closeable The closeable element
      * @param f A function that takes a closeable element and computes something
      * @tparam A The type of the closeable element. It must extends AutoCloseable
      * @tparam B The type of return of the f function
      * @return An element of type B
      */
    def tryWith[A <: AutoCloseable, B](closeable: A)(f: A => B) : B = {
        try { f(closeable) } finally { closeable.close() }
    }

    def tryWith[A <: AutoCloseable, B](closeables: Iterable[A])(f: Iterable[A] => B) : B = {
        try { f(closeables) } finally { closeables foreach (_.close())}
    }

}
