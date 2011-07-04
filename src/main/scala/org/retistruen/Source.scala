package org.retistruen

trait Source[T] extends Emitter[T] {

  def push(datum: Datum[T]): Unit =
    emit(datum)

  def push(some: T*): Unit =
    some foreach (emit(_))

  // Scala syntax sugar follows...

  def <<(datum: Datum[T]): Unit =
    push(datum)

  def <<(some: T): Unit =
    push(some)

  def <<<(some: T*): Unit =
    push(some: _*)

}
