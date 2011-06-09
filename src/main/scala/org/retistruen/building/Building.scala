package org.retistruen.building

import java.util.logging.Level
import org.joda.time.ReadablePeriod
import org.retistruen.instrument.reduce._
import org.retistruen.instrument._
import org.retistruen.{ Datum, Emitter, Named, Receiver }

/** Contains DSL methods for building [[org.retistruen.Model]] */
trait Building extends Named {

  type Em[T] = Emitter[T]
  type Re[T] = Receiver[T]

  protected var structure: Seq[Named] = Seq.empty

  private def register[N <: Named](named: N): N = {
    structure :+= named
    registered(named)
    named
  }

  protected def builder[E <: Em[_], R <: Re[_]](build: E ⇒ R): E ⇒ R =
    { e: E ⇒ register(build(e)) }

  protected def source[T](name: String)(implicit m: Manifest[T]) =
    register(new SourceEmitter[T](n(this, name)))

  protected def rec[T](implicit m: Manifest[T]) = builder { e: Em[T] ⇒ new RecordingReceiver[T](n(e, "rec")) }

  protected def rec[T](size: Int) = builder { e: Em[T] ⇒ new RecordingReceiver[T](n(e, "rec", size), Some(size)) }

  protected def max[T: Ordering] = builder { e: Em[T] ⇒ new AbsoluteMax[T](n(e, "max")) }

  protected def max[T: Ordering](size: Int) = builder { e: Em[T] ⇒ new SlidingMax[T](n(e, "max", size), size) }

  protected def mean[T: Fractional] = builder { e: Em[T] ⇒ new AbsoluteMean[T](n(e, "mean")) }

  protected def mean[T: Fractional](size: Int) = builder { e: Em[T] ⇒ new SlidingMean[T](n(e, "mean", size), size) }

  protected def log[T](level: Level) = builder { e: Em[T] ⇒ new Logger[T](n(e, "log"), level) }

  protected def collect[T](period: ReadablePeriod) = builder { e: Em[T] ⇒ new PeriodCollector[T](n(e, "collect", period), period) }

  protected def collect[T](size: Int) = builder { e: Em[T] ⇒ new SizeCollector[T](n(e, "collect", size), size) }

  protected def reduce[T, R](function: ReduceFunction[T, R]) = builder { e: Em[Seq[Datum[T]]] ⇒ new Reducer[T, R](n(e, "reduce", function), function) }

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

  protected def registered[T <: Named](instrument: T) = {}

  private def n(predecessor: Named, name: String, args: Any*): String = {
    val base = predecessor.name + "." + name
    if (args.isEmpty) base else base + args.mkString("(", ",", ")")
  }

  protected implicit def connector[T](emitter: Em[T]) = new Connector[T](emitter)

  protected class Connector[T](emitter: Em[T]) {
    def -->[Q <: Re[T]](builder: Em[T] ⇒ Q): Q = {
      val target = builder(emitter)
      emitter >> target
      target
    }
  }

}
