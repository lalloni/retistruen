package org.retistruen.instrument

import org.retistruen._

/** A max ranking functor over all received values historically. */
class AbsoluteMax[@specialized O: Ordering](val name: String)
    extends SimpleFunctor[O, O] {

  val o = implicitly[Ordering[O]]
  import o._

  override protected def operate(received: Datum[O]): Datum[O] = last match {
    case Some(previous) ⇒ if (received.value > previous.value) received.fresh else previous.fresh
    case None           ⇒ received.fresh
  }

}

/** An sliding window max ranking functor over the set of the last *size*
  * received values. */
class SlidingMax[@specialized O: Ordering](val name: String, val slide: Seq[Datum[O]] ⇒ Seq[Datum[O]])
    extends SlidingFunctor[O, O] {

  override protected def operate(data: Seq[Datum[O]]): Datum[O] =
    Datum(data.map(_.value).max)

}
