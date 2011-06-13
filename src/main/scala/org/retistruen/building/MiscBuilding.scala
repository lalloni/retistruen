package org.retistruen.building

import org.retistruen._
import org.retistruen.instrument._
import java.util.logging.Level

trait MiscBuilding {

  this: BuildingInfrastructure ⇒

  protected def rec[T] =
    register { e: Emitter[T] ⇒ new RecordingReceiver[T](receiverName(e, "rec")) }

  protected def rec[T](size: Int) =
    register { e: Emitter[T] ⇒ new RecordingReceiver[T](receiverName(e, "rec", size), Some(size)) }

  protected def log[T](level: Level) =
    register { e: Emitter[T] ⇒ new Logger[T](receiverName(e, "log"), level) }

}
