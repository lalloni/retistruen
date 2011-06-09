package org.retistruen

trait Collector[T] extends Receiver[T] with CachingEmitter[Seq[Datum[T]]] {

  private var buf: Seq[Datum[T]] = Seq.empty

  /** Returns the collected data in the internal buffer */
  protected def buffer =
    buf

  /** Clears the collected data from the internal buffer */
  protected def clear =
    buf = Seq.empty

  def receive(emitter: Emitter[T], datum: Datum[T]) =
    buf :+= datum

}
