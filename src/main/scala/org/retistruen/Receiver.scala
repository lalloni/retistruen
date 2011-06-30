package org.retistruen

/**
 * Represents an object capable of receiving typed values from an [[org.retistruen.Emitter]].
 *  @tparam T The type of the values this Receiver accepts.
 */
trait Receiver[T] extends Named {

  private var emitters: Seq[Emitter[T]] = Seq.empty

  def sources: Seq[Emitter[T]] = emitters

  /** Receives the emitted [[org.retistruen.Datum]] from the [[org.retistruen.Emitter]] */
  def receive(emitter: Emitter[T], datum: Datum[T])

  private[retistruen] def registered(emitter: Emitter[T]) =
    emitters :+= emitter

}

trait SlidingReceiver[T] extends Receiver[T] {

  private var data: Seq[Datum[T]] = Seq.empty

  val slide: Seq[Datum[T]] ⇒ Seq[Datum[T]]

  def window: Seq[Datum[T]] = data

  def windowValues: Seq[T] = window.map(_.value)

  def receive(emitter: Emitter[T], datum: Datum[T]) =
    data = slide(data :+ datum)

}
