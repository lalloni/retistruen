package org.retistruen.building

import org.retistruen._
import instrument._
import reduce._

trait ReducerBuilding {

  this: Building ⇒

  protected def reduce[T, R](name: String, function: Seq[T] ⇒ R) =
    register { e: Emitter[Seq[Datum[T]]] ⇒ new Reducer[T, R](receiverName(e, "reduce", name), { seq: Seq[Datum[T]] ⇒ Datum(function(seq.map(_.value))) }) }

  protected def reduce[T, R] = new ReducerBuilding[T, R]

  protected class ReducerBuilding[T, R] {
    private def builder[T, R](function: ReduceFunction[T, R]) = { e: Emitter[Seq[Datum[T]]] ⇒ new Reducer[T, R](receiverName(e, "reduce", function), function) }
    def sum[T: Numeric] = register(builder(new Sum[T]))
    def product[T: Numeric] = register(builder(new Product[T]))
    def max[T: Ordering] = register(builder(new Max[T]))
    def min[T: Ordering] = register(builder(new Min[T]))
    def mean[T: Fractional] = register(builder(new Mean[T]))
    def variance[T: Fractional] = register(builder(new Variance[T]))
    def stddev[T: Fractional] = register(builder(new StandardDeviation[T]))
    def skewness[T: Fractional] = register(builder(new Skewness[T]))
    def kurtosis[T: Fractional] = register(builder(new Kurtosis[T]))
  }

}
