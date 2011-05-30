package org.retistruen.dsl

import org.retistruen.Named
import org.retistruen.Emitter
import org.retistruen.Receiver
import org.retistruen.instrument.SourceEmitter
import org.retistruen.instrument.RecordingReceiver
import org.retistruen.instrument.AbsoluteMax
import org.retistruen.instrument.SlidingMax

/** Contains DSL methods for building [[org.retistruen.Model]] */
trait ModelBuilding {

  private var comps: Set[Named] = Set.empty

  def components: Set[Named] = comps

  def component(name: String) = comps.find(_.name == name)

  protected def source[T](name: String) =
    keep(new SourceEmitter[T](name))

  protected def rec[T] = { e: Emitter[T] ⇒
    keep(new RecordingReceiver[T](name(e, "rec")))
  }

  protected def rec[T](size: Int) = { e: Emitter[T] ⇒
    keep(new RecordingReceiver[T](name(e, "rec"), Some(size)))
  }

  protected def max[T: Ordering] = { e: Emitter[T] ⇒
    keep(new AbsoluteMax[T](name(e, "max")))
  }

  protected def max[T: Ordering](size: Int) = { e: Emitter[T] ⇒
    keep(new SlidingMax[T](name(e, "max" + size), size))
  }

  private def keep[N <: Named](named: N): N = {
    comps = comps + named
    named
  }

  private def name(previous: Emitter[_], name: String) = "%s.%s" format (previous.name, name)

  protected implicit def emitterConnector[T](emitter: Emitter[T]) = new Connector[T](emitter)

  protected class Connector[T](emitter: Emitter[T]) {
    def -->[Q <: Receiver[T]](builder: Emitter[T] ⇒ Q): Q = {
      val target = builder(emitter)
      emitter >> target
      target
    }
  }

}
