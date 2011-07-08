package org.retistruen.building

import org.retistruen._
import instrument._
import org.joda.time.ReadablePeriod

trait InstrumentBuilding {

  this: BuildingInfrastructure ⇒

  protected def max[T: Ordering] =
    register { e: Emitter[T] ⇒ new AbsoluteMax[T](receiverName(e, "max")) }

  protected def max[T: Ordering](size: Int) =
    register { e: Emitter[T] ⇒ new SlidingMax[T](receiverName(e, "max", size), Prune.bySize(size)) }

  protected def max[T: Ordering](period: ReadablePeriod) =
    register { e: Emitter[T] ⇒ new SlidingMax[T](receiverName(e, "max", period), Prune.byPeriod(period)) }

  protected def mean[T: Fractional] =
    register { e: Emitter[T] ⇒ new AbsoluteMean[T](receiverName(e, "mean")) }

  protected def mean[T: Fractional](size: Int) =
    register { e: Emitter[T] ⇒ new SlidingMean[T](receiverName(e, "mean", size), Prune.bySize(size)) }

  protected def mean[T: Fractional](period: ReadablePeriod) =
    register { e: Emitter[T] ⇒ new SlidingMean[T](receiverName(e, "mean", period), Prune.byPeriod(period)) }

}
