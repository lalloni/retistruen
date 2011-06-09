package org.retistruen.instrument

import org.retistruen.instrument.reduce.ReduceFunction
import org.retistruen.{ Datum, Emitter, Functor }
import org.retistruen.SimpleFunctor

class Reducer[T, R](val name: String, val function: ReduceFunction[T, R])
    extends SimpleFunctor[Seq[Datum[T]], R] {

  protected def operate(datum: Datum[Seq[Datum[T]]]): Datum[R] =
    function(datum.value)

}
