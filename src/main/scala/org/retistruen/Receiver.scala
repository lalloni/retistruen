package org.retistruen

/**
 * Represents an object capable of receiving typed values from an [[org.retistruen.Emitter]].
 *  @tparam T The type of the values this Receiver accepts.
 */
trait Receiver[T] extends Named {

  /** Receives the emitted [[org.retistruen.Datum]] from the [[org.retistruen.Emitter]] */
  def receive(emitter: Emitter[T], datum: Datum[T])

}

trait SlidingWindowReceiver[T] extends Receiver[T] {

  val size: Int

  private var data: Seq[Datum[T]] = Seq.empty

  def window: Seq[Datum[T]] = data

  def windowValues: Seq[T] = window.map(_.value)

  def receive(emitter: Emitter[T], datum: Datum[T]) = {
    data = data :+ datum
    data = data.drop(data.size - size)
  }

}
