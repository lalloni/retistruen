package org.retistruen.building

import org.retistruen._
import instrument._
import org.joda.time.ReadablePeriod

trait CollectorBuilding {

  this: BuildingInfrastructure ⇒

  protected def collect[T](period: ReadablePeriod) =
    register { e: Emitter[T] ⇒ new PeriodCollector[T](receiverName(e, "collect", period.toString.toLowerCase), period) }

  protected def collect[T](size: Int) =
    register { e: Emitter[T] ⇒ new SizeCollector[T](receiverName(e, "collect", size), size) }

}
