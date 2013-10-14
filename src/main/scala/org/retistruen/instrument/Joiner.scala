package org.retistruen.instrument

import org.retistruen.{ CachingEmitter, Datum, Emitter, Receiver, Reset }

import akka.actor.{ Actor, ActorSystem, Props, actorRef2Scala }
import grizzled.slf4j.Logging

class Joiner[T](val name: String)(implicit system: ActorSystem)
    extends Receiver[T]
    with CachingEmitter[Seq[Datum[T]]]
    with Reset with Logging {

  private case class Reception(emitter: Emitter[T], datum: Datum[T])

  private class Worker extends Actor {

    private[this] var data: Map[Emitter[T], Datum[T]] = Map.empty

    def receive = {
      case Reception(emitter, datum) ⇒
        data = data + (emitter → datum)
        val values = sources.map(data.get(_))
        if (!values.contains(None)) emit(Datum(values.map(_.get)))
      case Reset ⇒
        data = Map.empty
    }

  }

  val worker = system.actorOf(Props(new Worker))

  def receive(emitter: Emitter[T], datum: Datum[T]): Unit = {
    if (!sources.contains(emitter)) error("Emitter unknown: " + emitter)
    else worker ! Reception(emitter, datum)
  }

  override def reset {
    super.reset
    worker ! Reset
  }

}
