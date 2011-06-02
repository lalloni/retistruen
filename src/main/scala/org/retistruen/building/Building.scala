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

  protected var structure: Set[Named] = Set.empty

  protected def source[T](name: String) =
    keep(new SourceEmitter[T](postfix(this, name)))

  protected def rec[T] =
    { e: Emitter[T] ⇒ keep(new RecordingReceiver[T](postfix(e, "rec"))) }

  protected def rec[T](size: Int) =
    { e: Emitter[T] ⇒ keep(new RecordingReceiver[T](postfix(e, "rec" + size), Some(size))) }

  protected def max[T: Ordering] =
    { e: Emitter[T] ⇒ keep(new AbsoluteMax[T](postfix(e, "max"))) }

  protected def max[T: Ordering](size: Int) =
    { e: Emitter[T] ⇒ keep(new SlidingMax[T](postfix(e, "max" + size), size)) }

  protected def mean[T: Fractional] =
    { e: Emitter[T] ⇒ keep(new AbsoluteMean[T](postfix(e, "mean"))) }

  protected def mean[T: Fractional](size: Int) =
    { e: Emitter[T] ⇒ keep(new SlidingMean[T](postfix(e, "mean" + size), size)) }

  private def keep[N <: Named](named: N): N = {
    structure = structure + named
    named
  }

  private def postfix(previous: Named, name: String) =
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
