package org.retistruen.instrument

import org.retistruen._
import akka.actor.Actor
import akka.actor.Actor._

class Joiner[T](val name: String)
    extends Receiver[T]
    with CachingEmitter[Seq[Datum[T]]]
    with Start with Stop {

  private case class Reception(emitter: Emitter[T], datum: Datum[T])

  private class Worker extends Actor {

    var data: Map[Emitter[T], Datum[T]] = Map.empty

    def receive = {
      case Reception(emitter, datum) ⇒
        data = data + (emitter -> datum)
        val values = sources.map(data.get(_))
        if (!values.contains(None)) emit(Datum(values.map(_.get)))
    }

  }

  val worker = actorOf(new Worker)

  def receive(emitter: Emitter[T], datum: Datum[T]): Unit = {
    if (!worker.isRunning) sys.error("Joiner not started. Have you started your retistruen Model?")
    if (!sources.contains(emitter)) sys.error("Emitter unknown: " + emitter)
    worker ! Reception(emitter, datum)
  }

  def start = worker.start

  def stop = worker.stop

}
