package org.retistruen.instrument

import scala.concurrent.duration.DurationLong

import org.joda.time.ReadablePeriod
import org.retistruen.{ Collector, Datum, Emitter, Start, Stop }

import akka.actor.{ ActorSystem, Cancellable }
import grizzled.slf4j.{ Logger ⇒ Log }

class PeriodCollector[T](val name: String, val period: ReadablePeriod)(implicit system: ActorSystem)
    extends Collector[T]
    with Start
    with Stop {

  private implicit val ec = system.dispatcher

  private val log = Log(classOf[PeriodCollector[T]].getName + "." + name)

  private val duration = period.toPeriod.toStandardDuration.getMillis.millis

  private var schedule: Option[Cancellable] = None

  def start: Unit =
    schedule = Some(schedule.getOrElse(system.scheduler.schedule(duration, duration) {
      log.debug("Tick! Emitting buffer " + buffer.mkString("(", ", ", ")"))
      emit(buffer)
      clear
    }))

  def stop: Unit = {
    schedule.foreach(_.cancel)
    schedule = None
  }

  override def receive(emitter: Emitter[T], datum: Datum[T]) = {
    if (schedule.isEmpty) throw new IllegalStateException("PeriodCollector '" + name + "' must be started before use. Have you started your retistruen model?")
    super.receive(emitter, datum)
  }

}
