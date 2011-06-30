package org.retistruen.instrument

import org.retistruen._

class Slider[T](val name: String, val slide: Seq[Datum[T]] ⇒ Seq[Datum[T]])
  extends SlidingCollectorFunctor[T]
