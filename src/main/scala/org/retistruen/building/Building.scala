package org.retistruen.building

import java.util.logging.Level
import org.joda.time.ReadablePeriod
import org.retistruen.instrument.reduce._
import org.retistruen.instrument._
import org.retistruen.{ Datum, Emitter, Named, Receiver }

/** Contains DSL methods for building [[org.retistruen.Model]] */
trait Building extends Named {

  protected var structure: Seq[Named] = Seq.empty

  private def add[N <: Named](named: N): N = {
    structure :+= named
    registered(named)
    named
  }

  protected def registered[T <: Named](instrument: T) = {}

  protected def register[E <: Emitter[_], R <: Receiver[_]](builder: E ⇒ R): E ⇒ R =
    { e: E ⇒ add(builder(e)) }

  protected def source[T](name: String) =
    add(new SourceEmitter[T](name))

  protected implicit def connector[T](emitter: Emitter[T]) = new Connector[T](emitter)

  protected class Connector[T](emitter: Emitter[T]) {
    def -->[Q <: Receiver[T]](builder: Emitter[T] ⇒ Q): Q = {
      val target = builder(emitter)
      emitter >> target
      target
    }
  }

}
