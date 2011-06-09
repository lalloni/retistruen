package org.retistruen.instrument.reduce

import org.retistruen.Datum
import org.retistruen.Named

package object algo {

  def pow[T: Numeric](base: T, exponent: Int) = {
    val t = implicitly[Numeric[T]]
    var product = t.one
    for (i ← 1 to exponent) product = t.times(product, base)
    product
  }

  def sum[T: Numeric](seq: Seq[T]) = seq.sum

  def size(seq: Seq[_]) = seq.size

  def mean[T: Fractional](seq: Seq[T]) = {
    val t = implicitly[Fractional[T]]
    t.div(sum(seq), t.fromInt(size(seq)))
  }

  def moment[T: Fractional](k: Int)(seq: Seq[T]): T = {
    val t = implicitly[Fractional[T]]
    val u = mean(seq)
    seq.map(e ⇒ pow(t.minus(e, u), k)).sum
  }

  def variance[T: Fractional](seq: Seq[T]) = moment(2)(seq)

  def skewness[T: Fractional](seq: Seq[T]) = moment(3)(seq)

  def kurtosis[T: Fractional](seq: Seq[T]) = moment(4)(seq)

  def standardDeviation[T: Fractional](seq: Seq[T]) =
    math.sqrt(implicitly[Fractional[T]].toDouble(variance(seq)))

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
