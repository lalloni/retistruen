package org.retistruen.instrument.reduce

import org.retistruen.Datum

package object algo {

  def intpow[T: Numeric](base: T, exponent: Int): T = {
    assert(exponent >= 0, "Only zero and positives are supported as exponent")
    val t = implicitly[Numeric[T]]; import t._
    var product = t.one
    for (i ← 1 to exponent) product = product * base
    product
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
    seq.map(e ⇒ intpow(e - u, k)).sum
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

  def percentRank[T: Fractional](size: Int)(n: Int): T = {
    assert(size > 0, "Size must be greater than zero to calculate percent rank")
    assert(n >= 1 && n <= size, "Ordinal must be between 0 and size (inclusive): " + n)
    val t = implicitly[Fractional[T]]; import t._
    (fromInt(100) / fromInt(size)) * (fromInt(n) - (one / fromInt(2)))
  }

  /** Implements the algorithm described in http://en.wikipedia.org/wiki/Percentile#Nearest_rank */
  def roundedPercentile[T: Ordering](p: Int)(seq: Seq[T]): T = {
    assert(!seq.isEmpty, "Can not calculate the mean of an empty sequence")
    assert(p >= 0 && p <= 100, "Percentile must be between 0 and 100 (inclusive): " + p)
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
    val t = implicitly[Fractional[T]]; import t._
    val sorted = seq.sorted
    // one-based indexing of sorted
    def value(i: Int) = sorted(i - 1)
    val size = seq.size
    val rank = percentRank[T](size)(_)
    val P = t.fromInt(p)
    if (P < rank(1)) {
      sorted.head
    } else if (P > rank(size)) {
      sorted.last
    } else {
      (1 to size)
        .find(k ⇒ rank(k) == P || rank(k) < P && P < rank(k + 1))
        .map(k ⇒
          if (P == rank(k)) value(k)
          else value(k) + fromInt(size) * (((P - rank(k)) / fromInt(100)) * (value(k + 1) - value(k))))
        .get
    }
  }

  def interpolatedQuartile[T: Fractional](q: Int)(values: Seq[T]) = {
    assert(q >= 0 && q <= 4, "Quartile must be between 0 and 4 (inclusive): " + q)
    interpolatedPercentile[T](q * 25)(values)
  }

  def interpolatedMedian[T: Fractional](values: Seq[T]) =
    interpolatedPercentile[T](50)(values)

}
