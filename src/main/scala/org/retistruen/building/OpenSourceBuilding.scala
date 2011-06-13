package org.retistruen.building

import org.retistruen.instrument.OpenSourceEmitter
import org.retistruen.{ Emitter, OpenSource, ReadableFromString }

trait OpenSourceBuilding {

  this: BuildingInfrastructure ⇒

  protected def osource[T: ReadableFromString](name: String) =
    register(new OpenSourceEmitter[T](name))

  protected def osource[T](name: String, reader: String ⇒ T) = {
    implicit val readable = new ReadableFromString[T] { def read(s: String) = reader.apply(s) }
    register(new OpenSourceEmitter[T](name))
  }

}
