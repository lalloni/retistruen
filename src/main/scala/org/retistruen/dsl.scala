package org.retistruen

class Model {

  type E[T] = Emitter[T]
  type R[T] = Receiver[T]
  type RE[T] = Receiver[T] with Emitter[T]
  type ER[T] = E[T] ⇒ R[T]
  type ERE[T] = E[T] ⇒ RE[T]

  def source[T](name: String): E[T] = new SourceEmitter[T](name)

  def rec[T, R]: ER[T] = { e ⇒ new RecordingReceiver[T](name(e, "rec")) }

  def max[T: Ordering]: ERE[T] = { e ⇒ new AbsoluteMax[T](name(e, "max")) }

  def max[T: Ordering](size: Int): ERE[T] = { e ⇒ new SlidingMax[T](name(e, "max" + size), size) }

  private def name(previous: E[_], tag: String) = previous.name + "." + tag

  implicit def emitterConnector[T](emitter: E[T]) = new Connector[T](emitter)

  class Connector[T](emitter: E[T]) {
    def -->(builder: ERE[T]): E[T] = emitter >> builder(emitter)
    def --|(builder: ERE[T]): E[T] = emitter >> builder(emitter)
  }

}

object Sensors extends Model {

  val s1 = source[BigDecimal]("s1")
  val s2 = source[BigDecimal]("s2")

  s1 --> max

  s1 --> max(10)

}
