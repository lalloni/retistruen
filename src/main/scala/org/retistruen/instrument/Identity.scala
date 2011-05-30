package org.retistruen.instrument

import org.retistruen.Datum
import org.retistruen.SimpleFunctor

/** The (roughly) identity [[org.retistruen.Functor]].
 *  Emits the received data unmodified. */
class Identity[@specialized T](val name: String) extends SimpleFunctor[T, T] {

  override protected def operate(datum: Datum[T]): Datum[T] = datum.fresh

}
