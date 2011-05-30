/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 30/05/2011 15:16:29
 */
package org.retistruen.instrument

import org.retistruen.Datum
import org.retistruen.SimpleFunctor
import org.apache.commons.math.stat.descriptive.moment.Mean
import org.joda.time.Instant

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 * @since 30/05/2011 15:16:29
 */
class AbsoluteMean[@specialized N: Numeric](val name: String) extends SimpleFunctor[N, Double] {

  val num = implicitly[Numeric[N]]

  val mean = new Mean

  override protected def operate(datum: Datum[N]): Datum[Double] = {
    mean.increment(num.toDouble(datum.value))
    datum.freshWith(mean.getResult)
  }

}
