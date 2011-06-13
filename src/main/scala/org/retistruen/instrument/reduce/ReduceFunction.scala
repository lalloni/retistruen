package org.retistruen.instrument.reduce

import org.retistruen.Datum
import org.retistruen.Named

package object algo {

  def intpow[T: Numeric](base: T, exponent: Int): T = {
    assert(exponent >= 0, "Only zero and positives are supported as exponent")
    val t = implicitly[Numeric[T]]; import t._
    var product = t.one
    for (i ← 1 to exponent) product = product * base
    product
  }

  def mean[T: Fractional](seq: Seq[T]) = {
    assert(!seq.isEmpty, "Can not calculate the mean of an empty sequence")
    val t = implicitly[Fractional[T]]; import t._
    seq.sum / t.fromInt(seq.size)
  }

  def moment[T: Fractional](k: Int)(seq: Seq[T]): T = {
    assert(!seq.isEmpty, "Can not calculate a moment of an empty sequence")
    val t = implicitly[Fractional[T]]; import t._
    val u = mean(seq)
    seq.map(e ⇒ intpow(e - u, k)).sum
  }

  def variance[T: Fractional](seq: Seq[T]) = moment(2)(seq)

  def skewness[T: Fractional](seq: Seq[T]) = moment(3)(seq)

  def kurtosis[T: Fractional](seq: Seq[T]) = moment(4)(seq)

  def standardDeviation[T: Fractional](seq: Seq[T]) =
    math.sqrt(implicitly[Fractional[T]].toDouble(variance(seq)))

  def percentRank[T: Fractional](size: Int)(o: Int): T = {
    assert(size > 0, "Size must be greater than zero to calculate percent rank")
    assert(o >= 0 && o <= size, "Ordinal must be between 0 and size (inclusive): " + o)
    val t = implicitly[Fractional[T]]; import t._
    (t.fromInt(100) / t.fromInt(size)) * (t.fromInt(o) - (t.one / t.fromInt(2)))
  }

  def roundedPercentile[T: Ordering](p: Int)(seq: Seq[T]): T = {
    assert(!seq.isEmpty, "Can not calculate the mean of an empty sequence")
    assert(p >= 0 && p <= 100, "Percentile must be between 0 and 100 (inclusive): " + p)
    val size = seq.size
    val value = seq.sorted
    val ordinalRank = math.round((p / 100.0) * size + 0.5).toInt
    value(ordinalRank)
  }

  def interpolatedPercentile[T: Fractional](p: Int)(seq: Seq[T]): T = {
    assert(!seq.isEmpty, "Can not calculate the mean of an empty sequence")
    assert(p >= 0 && p <= 100, "Percentile must be between 0 and 100 (inclusive): " + p)
    val t = implicitly[Fractional[T]]; import t._
    val value = seq.sorted
    val size = value.size
    val baseOrdinalRank = math.floor((p / 100.0) * size).toInt
    val nextOrdinalRank = baseOrdinalRank + 1
    val rank = percentRank[T](size)(_)
    val baseValue = value(baseOrdinalRank)
    val nextValue = value(nextOrdinalRank)
    val baseRank = rank(baseOrdinalRank)
    val nextRank = rank(nextOrdinalRank)
    baseValue +
      t.fromInt(size) * (nextRank - baseRank) /
      t.fromInt(100) * (nextValue - baseValue)
  }

  def interpolatedQuartile[T: Fractional](q: Int)(values: Seq[T]) = {
    assert(q >= 0 && q <= 4, "Quartile must be between 0 and 4 (inclusive): " + q)
    interpolatedPercentile[T](q * 25)(values)
  }

  def interpolatedMedian[T: Fractional](values: Seq[T]) =
    interpolatedPercentile[T](50)(values)

}

trait ReduceFunction[T, R] extends (Seq[Datum[T]] ⇒ Datum[R]) with Named

class Max[T: Ordering] extends ReduceFunction[T, T] {
  val name = "max"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(data.map(_.value).max)
}

class Min[T: Ordering] extends ReduceFunction[T, T] {
  val name = "min"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(data.map(_.value).min)
}

class Mean[T: Fractional] extends ReduceFunction[T, T] {
  val name = "mean"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(algo.mean(data.map(_.value)))
}

class Variance[T: Fractional] extends ReduceFunction[T, T] {
  val name = "variance"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(algo.variance(data.map(_.value)))
}

class Skewness[T: Fractional] extends ReduceFunction[T, T] {
  val name = "variance"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(algo.skewness(data.map(_.value)))
}

class Kurtosis[T: Fractional] extends ReduceFunction[T, T] {
  val name = "variance"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(algo.kurtosis(data.map(_.value)))
}

class StandardDeviation[T: Fractional] extends ReduceFunction[T, Double] {
  val name = "stddev"
  def apply(data: Seq[Datum[T]]): Datum[Double] =
    Datum(algo.standardDeviation(data.map(_.value)))
}

class Sum[T: Numeric] extends ReduceFunction[T, T] {
  val name = "sum"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(data.map(_.value).sum)
}

class Product[T: Numeric] extends ReduceFunction[T, T] {
  val name = "product"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(data.map(_.value).product)
}

class Open[T] extends ReduceFunction[T, T] {
  val name = "open"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(data.head.value)
}

class Close[T] extends ReduceFunction[T, T] {
  val name = "close"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(data.last.value)
}

class Mode[T] extends ReduceFunction[T, T] {
  val name = "mode"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(data.map(_.value).groupBy(identity).maxBy(_._2.size)._1)
}

class Median[T: Fractional] extends ReduceFunction[T, T] {
  val name = "median"
  val t = implicitly[Fractional[T]]; import t._
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(algo.interpolatedMedian(data.map(_.value)))
}

class Range[T: Numeric] extends ReduceFunction[T, T] {
  val name = "range"
  val t = implicitly[Numeric[T]]; import t._
  def apply(data: Seq[Datum[T]]): Datum[T] = {
    val values = data.map(_.value)
    Datum(values.max - values.min)
  }
}

class Percentile[T: Fractional](p: Int) extends ReduceFunction[T, T] {
  val name = "percentile"
  val t = implicitly[Numeric[T]]; import t._
  def apply(data: Seq[Datum[T]]): Datum[T] = {
    val values = data.map(_.value)
    Datum(algo.interpolatedPercentile(p)(values))
  }
}
