/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 30/05/2011 15:16:29
 */
package org.retistruen.instrument

import org.retistruen.Datum
import org.retistruen.SimpleFunctor
import org.apache.commons.math.stat.descriptive.moment.Mean
import org.joda.time.Instant
import org.retistruen.SequenceFunctor

class AbsoluteMean[@specialized N: Numeric](val name: String) extends SimpleFunctor[N, Double] {

  val num = implicitly[Numeric[N]]
  import num._

  val mean = new Mean

  override protected def operate(datum: Datum[N]): Datum[Double] = {
    mean.increment(datum.value.toDouble)
    datum.freshWith(mean.getResult)
  }

}

class SlidingMean[@specialized N: Numeric](val name: String, val size: Int) extends SequenceFunctor[N, Double] {

  val num = implicitly[Numeric[N]]
  import num._

  override protected def operate(datums: Seq[Datum[N]]): Datum[Double] = {
    val mean = new Mean
    for (datum ← datums) mean.increment(datum.value.toDouble)
    Datum(mean.getResult)
  }

}
