package org.retistruen.instrument

import org.retistruen._

class Reducer[T, R](val name: String, val function: Seq[Datum[T]] ⇒ Datum[R])
    extends Functor[Seq[Datum[T]], R] with CachingEmitter[R] {

  def receive(emitter: Emitter[Seq[Datum[T]]], datum: Datum[Seq[Datum[T]]]) =
    if (!datum.value.isEmpty) emit(function(datum.value))

}
