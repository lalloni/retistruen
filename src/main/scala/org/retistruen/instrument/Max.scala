package org.retistruen.instrument

import org.retistruen.Datum
import org.retistruen.SequenceFunctor
import org.retistruen.SimpleFunctor

/** An sliding window max ranking functor over the set of the last *size*
 *  received values.
 */
class SlidingMax[@specialized O: Ordering](val name: String, val size: Int) extends SequenceFunctor[O, O] {
  override protected def operate(data: Seq[Datum[O]]): Datum[O] = Datum(data.map(_.value).max)
}

/** A max ranking functor over all received values historically. */
class AbsoluteMax[@specialized O: Ordering](val name: String) extends SimpleFunctor[O, O] {
  val o = implicitly[Ordering[O]]
  import o._
  override protected def operate(received: Datum[O]): Datum[O] = last match {
    case Some(previous) ⇒ if (received.value > previous.value) received.refresh else previous.refresh
    case None           ⇒ received.refresh
  }
}
