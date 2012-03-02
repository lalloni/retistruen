/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 30/05/2011 15:16:29
 */
package org.retistruen.instrument

import org.joda.time.Instant
import org.retistruen._

class AbsoluteMean[@specialized F: Fractional](val name: String) extends SimpleFunctor[F, F] with Reset {

  val f = implicitly[Fractional[F]]
  import f._

  var mean: F = f.zero

  var count: Int = 0

  override protected def operate(datum: Datum[F]): Datum[F] = {
    count = count + 1
    mean = mean + (datum.value - mean) / f.fromInt(count)
    Datum(mean)
  }

  override def reset {
    super.reset
    mean = f.zero
    count = 0
  }

}

class SlidingMean[@specialized F: Fractional](val name: String, val slide: Seq[Datum[F]] ⇒ Seq[Datum[F]]) extends SlidingFunctor[F, F] {

  val f = implicitly[Fractional[F]]
  import f._

  override protected def operate(datums: Seq[Datum[F]]): Datum[F] =
    Datum(datums.map(_.value).sum / f.fromInt(datums.size))

}
