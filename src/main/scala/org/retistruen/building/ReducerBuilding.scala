package org.retistruen.building

import org.retistruen._
import instrument._
import reduce._

trait ReducerBuilding {

  this: Building ⇒

  protected def reduce[T, R](function: ReduceFunction[T, R]) = register { e: Emitter[Seq[Datum[T]]] ⇒ new Reducer[T, R](receiverName(e, "reduce", function), function) }

  protected object r {
    def sum[T: Numeric] = new Sum[T]
    def product[T: Numeric] = new Product[T]
    def max[T: Ordering] = new Max[T]
    def min[T: Ordering] = new Min[T]
    def mean[T: Fractional] = new Mean[T]
    def variance[T: Fractional] = new Variance[T]
    def stddev[T: Fractional] = new StandardDeviation[T]
    def skewness[T: Fractional] = new Skewness[T]
    def kurtosis[T: Fractional] = new Kurtosis[T]
  }

}
