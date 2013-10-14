/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 02/06/2011 11:14:35
 */
package org.retistruen.survey

import scala.concurrent.duration.DurationLong

import org.joda.time.{ Instant, ReadablePeriod }
import org.retistruen.{ Datum, Source, Tagging }

import akka.actor.{ Actor, actorRef2Scala }
import grizzled.slf4j.Logging

case object Stop

sealed trait Chronometric {
  val instant = new Instant
  override def toString = s"${getClass.getSimpleName}($instant)"
}

private class Tick extends Chronometric

class Beat extends Chronometric

class FrequencySurvey(target: Source[Int], period: ReadablePeriod, tagging: Option[Tagging] = None) extends Actor with Logging {

  private val duration = period.toPeriod.toStandardDuration

  private implicit val ec = context.system.dispatcher

  private var beats: Seq[Beat] = Seq.empty

  private val sd = duration.getMillis.millis

  private var schedule = context.system.scheduler.schedule(sd, sd) { self ! new Tick }

  def receive = {
    case tick: Tick ⇒
      beats = beats.filter(_.instant isAfter tick.instant.minus(duration))
      target << Datum(beats.count(_.instant isBefore tick.instant), tick.instant, tagging)
    case beat: Beat ⇒
      beats :+= beat
    case s @ Stop ⇒
      schedule.cancel
      context.stop(self)
  }

}
