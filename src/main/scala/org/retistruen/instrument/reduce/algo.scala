package org.retistruen.instrument.reduce

import org.retistruen.Datum

package object algo {

  def pow[T: Numeric](base: T, exponent: Int): T = {
    assert(exponent >= 0, "Only zero and positives are supported as exponent")
    val t = implicitly[Numeric[T]];
    import t._
    Stream.continually(base).take(exponent).product
  }

  /** See http://en.wikipedia.org/wiki/Arithmetic_mean */
  def mean[T: Fractional](seq: Seq[T]) = {
    assert(!seq.isEmpty, "Can not calculate the mean of an empty sequence")
    val t = implicitly[Fractional[T]]; import t._
    seq.sum / t.fromInt(seq.size)
  }

  /** See http://en.wikipedia.org/wiki/Moment_(mathematics) */
  def moment[T: Fractional](k: Int)(seq: Seq[T]): T = {
    assert(!seq.isEmpty, "Can not calculate a moment of an empty sequence")
    val t = implicitly[Fractional[T]]; import t._
    val u = mean(seq)
    seq.map(e ⇒ pow(e - u, k)).sum
  }

  /** See http://en.wikipedia.org/wiki/Variance */
  def variance[T: Fractional](seq: Seq[T]) = moment(2)(seq)

  /** See http://en.wikipedia.org/wiki/Skewness */
  def skewness[T: Fractional](seq: Seq[T]) = moment(3)(seq)

  /** See http://en.wikipedia.org/wiki/Kurtosis */
  def kurtosis[T: Fractional](seq: Seq[T]) = moment(4)(seq)

  /** See http://en.wikipedia.org/wiki/Standard_deviation */
  def standardDeviation[T: Fractional](seq: Seq[T]) =
    math.sqrt(implicitly[Fractional[T]].toDouble(variance(seq)))

  def percentRank[T: Fractional](size: Int, index: Int): T = {
    assert(size > 0, "Size must be greater than zero to calculate percent rank")
    assert(index >= 1 && index <= size, "Index must be from 1 to %s (inclusive)" format size)
    val t = implicitly[Fractional[T]]; import t._
    (fromInt(100) / fromInt(size)) * (fromInt(index) - one / fromInt(2))
  }

  def percentIndex[T: Fractional](size: Int, r: T): Int = {
    val t = implicitly[Fractional[T]]; import t._
    assert(size > 0, "Size must be greater than zero to calculate percent index")
    assert(r >= one && r <= fromInt(100), "Rank must be between 1 and 100 (inclusive)")
    (r * (fromInt(size) / fromInt(100)) + one / fromInt(2)).toInt
  }

  /** Implements the algorithm described in http://en.wikipedia.org/wiki/Percentile#Nearest_rank */
  def roundedPercentile[T: Ordering](p: Int)(seq: Seq[T]): T = {
    assert(!seq.isEmpty, "Can not calculate the mean of an empty sequence")
    assert(p >= 1 && p <= 100, "Percentile must be from 1 to 100 (inclusive)")
    val size = seq.size
    val value = seq.sorted
    val ordinalRank = math.round((p / 100.0) * size + 0.5).toInt
    value(ordinalRank)
  }

  /** See http://en.wikipedia.org/wiki/Percentile
    * Implements http://en.wikipedia.org/wiki/Percentile#Linear_interpolation_between_closest_ranks
    */
  def interpolatedPercentile[T: Fractional](p: Int)(seq: Seq[T]): T = {
    assert(!seq.isEmpty, "Can not calculate the interpolated percentile of an empty sequence")
    assert(p >= 1 && p <= 100, "Percentile must be from 1 to 100")
    val t = implicitly[Fractional[T]]; import t._
    val sorted = seq.sorted
    val size = seq.size
    def interpolate(k: Int) = {
      val rk = percentRank(size, k)
      val pp = fromInt(p)
      val vk = sorted(k - 1)
      if (rk == pp) vk
      else {
        val S = fromInt(size)
        val C = fromInt(100)
        val vk1 = sorted(k)
        vk + S * (((pp - rk) / C) * (vk1 - vk))
      }
    }
    size match {
      case 1 ⇒ seq.head
      case 2 ⇒ interpolate(1)
      case _ ⇒ interpolate(percentIndex(size, fromInt(p)))
    }
  }

  def interpolatedQuartile[T: Fractional](q: Int)(values: Seq[T]) = {
    assert(q >= 1 && q <= 4, "Quartile must be from 1 to 4")
    interpolatedPercentile[T](q * 25)(values)
  }

  def interpolatedMedian[T: Fractional](values: Seq[T]) =
    interpolatedPercentile[T](50)(values)

}
