package org.retistruen.building

import org.retistruen.Named
import org.retistruen.Emitter
import org.retistruen.Receiver
import org.retistruen.instrument.SourceEmitter
import org.retistruen.instrument.RecordingReceiver
import org.retistruen.instrument.AbsoluteMax
import org.retistruen.instrument.SlidingMax
import org.retistruen.instrument.SlidingMean
import org.retistruen.instrument.AbsoluteMean

/** Contains DSL methods for building [[org.retistruen.Model]] */
trait Building extends Named {

  protected var structure: Seq[Named] = Seq.empty

  private def register[N <: Named](named: N): N = {
    structure :+= named
    registered(named)
    named
  }

  protected def builder[E <: Emitter[_], R <: Receiver[_]](build: E ⇒ R): E ⇒ R =
    { e: E ⇒ register(build(e)) }

  protected def source[T](name: String) =
    register(new SourceEmitter[T](suffix(this, name)))

  protected def rec[T] = builder { e: Emitter[T] ⇒ new RecordingReceiver[T](suffix(e, "rec")) }

  protected def rec[T](size: Int) = builder { e: Emitter[T] ⇒ new RecordingReceiver[T](suffix(e, "rec" + size), Some(size)) }

  protected def max[T: Ordering] = builder { e: Emitter[T] ⇒ new AbsoluteMax[T](suffix(e, "max")) }

  protected def max[T: Ordering](size: Int) = builder { e: Emitter[T] ⇒ new SlidingMax[T](suffix(e, "max" + size), size) }

  protected def mean[T: Fractional] = builder { e: Emitter[T] ⇒ new AbsoluteMean[T](suffix(e, "mean")) }

  protected def mean[T: Fractional](size: Int) = builder { e: Emitter[T] ⇒ new SlidingMean[T](suffix(e, "mean" + size), size) }

  protected def registered[T <: Named](instrument: T) = {}

  private def suffix(previous: Named, name: String) =
    "%s.%s" format (previous.name, name)

  protected implicit def connector[T](emitter: Emitter[T]) = new Connector[T](emitter)

  protected class Connector[T](emitter: Emitter[T]) {
    def -->[Q <: Receiver[T]](builder: Emitter[T] ⇒ Q): Q = {
      val target = builder(emitter)
      emitter >> target
      target
    }
  }

}
