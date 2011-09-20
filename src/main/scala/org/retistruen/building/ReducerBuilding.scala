package org.retistruen.building

import org.retistruen._
import instrument._
import reduce._

trait ReducerBuilding {

  this: BuildingInfrastructure ⇒

  protected def reduce[T, R](name: String, function: Seq[T] ⇒ R) =
    register { e: Emitter[Seq[Datum[T]]] ⇒ new Reducer[T, R](receiverName(e, "reduce", name), { seq: Seq[Datum[T]] ⇒ Datum(function(seq.map(_.value))) }) }

  protected def reduce[T, R] = new ReducerBuilding[T, R]

  protected class ReducerBuilding[T, R] {
    private[ReducerBuilding] def builder[T, R](function: ReduceFunction[T, R], args: Any*) = { e: Emitter[Seq[Datum[T]]] ⇒ new Reducer[T, R](receiverName(e, "reduce", (function +: args): _*), function) }
    def sum[T: Numeric] = register(builder(new Sum[T]))
    def product[T: Numeric] = register(builder(new Product[T]))
    def max[T: Ordering] = register(builder(new Max[T]))
    def min[T: Ordering] = register(builder(new Min[T]))
    def mean[T: Fractional] = register(builder(new Mean[T]))
    def variance[T: Fractional] = register(builder(new Variance[T]))
    def stddev[T: Fractional] = register(builder(new StandardDeviation[T]))
    def skewness[T: Fractional] = register(builder(new Skewness[T]))
    def kurtosis[T: Fractional] = register(builder(new Kurtosis[T]))
    def open[T] = register(builder(new Open[T]))
    def close[T] = register(builder(new Close[T]))
    def mode[T] = register(builder(new Mode[T]))
    def median[T: Fractional] = register(builder(new Median[T]))
    def percentile[T: Fractional](n: Int) = register(builder(new Percentile[T](n), n))
    def range[T: Numeric] = register(builder(new Range[T]))
  }

}
