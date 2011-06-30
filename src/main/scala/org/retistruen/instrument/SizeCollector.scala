package org.retistruen.instrument

import org.retistruen._

class SizeCollector[T](val name: String, val size: Int) extends Collector[T] {

  override def receive(em: Emitter[T], dat: Datum[T]) = {
    super.receive(em, dat)
    if (buffer.size == size) {
      emit(buffer)
      clear
    }
  }

}
