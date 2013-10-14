package org.retistruen.building

import java.util.logging.Level
import org.joda.time.ReadablePeriod
import org.retistruen._
import instrument._
import reduce._
import akka.actor.ActorSystem

/** Contains DSL methods for building [[org.retistruen.Model]] */
trait BuildingInfrastructure {

  this: Named ⇒

  implicit val actorSystem: ActorSystem

  private var comps: Seq[Named] = Seq.empty

  final def components = comps

  protected def select[T](implicit m: Manifest[T]): Seq[T] = components
    .filter(m.erasure.isInstance(_))
    .map(_.asInstanceOf[T])

  protected final def register[N <: Named](component: N): N = {
    comps :+= component
    registered(component)
    component
  }

  protected def registered[T <: Named](instrument: T): Unit = {}

  protected final def register[E <: Emitter[_], R <: Receiver[_]](builder: E ⇒ R): E ⇒ R =
    { e: E ⇒ register(builder.apply(e)) }

  protected final def source[T](name: String) =
    register(new SourceEmitter[T](name))

  protected final implicit def connector[T](emitter: Emitter[T]) = new Connector[T](emitter)

  protected final class Connector[T](emitter: Emitter[T]) {
    def -->[Q <: Receiver[T]](builder: Emitter[T] ⇒ Q): Q = {
      val target = builder(emitter)
      emitter >> target
      target
    }
  }

}
