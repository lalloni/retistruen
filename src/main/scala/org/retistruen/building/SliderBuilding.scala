package org.retistruen.building

import org.retistruen._
import instrument._
import org.joda.time.ReadablePeriod

trait SliderBuilding {

  this: BuildingInfrastructure ⇒

  protected def slide[T](period: ReadablePeriod) =
    register { e: Emitter[T] ⇒ new Slider[T](receiverName(e, "slide", period.toString.toLowerCase), Prune.byPeriod(period)) }

  protected def slide[T](size: Int) =
    register { e: Emitter[T] ⇒ new Slider[T](receiverName(e, "slide", size), Prune.bySize(size)) }

}
