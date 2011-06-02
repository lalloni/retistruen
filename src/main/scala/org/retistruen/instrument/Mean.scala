/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 30/05/2011 15:16:29
 */
package org.retistruen.instrument

import org.retistruen.Datum
import org.retistruen.SimpleFunctor
import org.joda.time.Instant
import org.retistruen.SequenceFunctor

class AbsoluteMean[@specialized N: Fractional](val name: String) extends SimpleFunctor[N, N] {

  val F = implicitly[Fractional[N]]

  import F._

  var mean: N = F.zero

  var count: Int = 0

  override protected def operate(datum: Datum[N]): Datum[N] = {
    count = count + 1
    mean = mean + (datum.value - mean) / F.fromInt(count)
    Datum(mean)
  }

}

class SlidingMean[@specialized N: Fractional](val name: String, val size: Int) extends SequenceFunctor[N, N] {

  val F = implicitly[Fractional[N]]
  import F._

  override protected def operate(datums: Seq[Datum[N]]): Datum[N] = {
    Datum(datums.map(_.value).sum / F.fromInt(datums.size))
  }

}
