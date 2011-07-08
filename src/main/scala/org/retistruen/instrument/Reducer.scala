package org.retistruen.instrument

import org.retistruen._

class Reducer[T, R](val name: String, val function: Seq[Datum[T]] ⇒ Datum[R])
    extends SimpleFunctor[Seq[Datum[T]], R] {

  protected def operate(datum: Datum[Seq[Datum[T]]]): Datum[R] =
    function(datum.value)

}
