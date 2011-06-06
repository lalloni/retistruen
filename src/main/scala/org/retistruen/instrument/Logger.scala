package org.retistruen.instrument

import org.retistruen.{ Emitter, Receiver, Datum }
import java.util.logging.{ Logger ⇒ LoggerFactory, Level }

class Logger[T](val name: String, var level: Level) extends Receiver[T] {

  val logger = LoggerFactory.getLogger(name)

  def receive(emitter: Emitter[T], datum: Datum[T]) =
    logger.log(level, datum.value.toString)

}
