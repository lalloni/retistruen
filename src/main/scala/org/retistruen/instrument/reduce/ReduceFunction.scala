package org.retistruen.instrument.reduce

import org.retistruen.Datum
import org.retistruen.Named

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
  val name = "skewness"
  def apply(data: Seq[Datum[T]]): Datum[T] =
    Datum(algo.skewness(data.map(_.value)))
}

class Kurtosis[T: Fractional] extends ReduceFunction[T, T] {
  val name = "kurtosis"
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
    Datum(data.map(_.value).groupBy(identity).max(new Ordering[(Any, Seq[Any])] {
      def compare(x: (Any, Seq[Any]), y: (Any, Seq[Any])): Int =
        x._2.size compare y._2.size
    })._1)
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
