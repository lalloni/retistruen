package org.retistruen.building

import org.retistruen._
import instrument._

trait InstrumentBuilding {

  this: BuildingInfrastructure ⇒

  protected def max[T: Ordering] =
    register { e: Emitter[T] ⇒ new AbsoluteMax[T](receiverName(e, "max")) }

  protected def max[T: Ordering](size: Int) =
    register { e: Emitter[T] ⇒ new SlidingMax[T](receiverName(e, "max", size), size) }

  protected def mean[T: Fractional] =
    register { e: Emitter[T] ⇒ new AbsoluteMean[T](receiverName(e, "mean")) }

  protected def mean[T: Fractional](size: Int) =
    register { e: Emitter[T] ⇒ new SlidingMean[T](receiverName(e, "mean", size), size) }

}
