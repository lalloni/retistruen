package org.retistruen.instrument

import org.joda.time.ReadablePeriod
import org.retistruen.Collector
import scala.actors.Actor._
import scala.actors.TIMEOUT
import org.retistruen.Emitter
import org.retistruen.Datum

class PeriodCollector[T](val name: String, val period: ReadablePeriod) extends Collector[T] {

  actor {
    loop {
      reactWithin(period.toPeriod.toStandardDuration.getMillis) {
        case TIMEOUT ⇒
          emit(buffer)
          clear
      }
    }
  }

}
