package org.retistruen

import java.util.Calendar
import org.joda.time.Instant
import org.joda.time.ReadableInstant
import scala.collection.immutable.Queue
import scala.actors.Actor

case class Datum[T](value: T, created: ReadableInstant = new Instant) {
  def fresh = Datum(value)
}

trait Named {

  val name: String

}

trait Receiver[T] extends Named {

  def receive(emitter: Emitter[T], datum: Datum[T])

}

trait Emitter[T] extends Named {

  private var receivers: Set[Receiver[T]] = Set.empty

  private def register(rec: Receiver[T]): Unit =
    receivers = receivers + rec

  def >>>(others: Receiver[T]*): Unit = others.foreach(register(_))

  def >>(other: Receiver[T]): Unit = register(other)

  def >>(other: Receiver[T] with Emitter[T]): Emitter[T] = {
    register(other)
    other
  }

  def emit(datum: Datum[T]): Unit =
    receivers.foreach(_.receive(this, datum))

  def emit(some: T): Unit =
    emit(Datum(some, new Instant))

  def <<(datum: Datum[T]) = emit(datum)

  def <<(some: T) = emit(some)

  def <<<(some: T*) = some foreach emit

}

trait BufferedReceiver[T] extends Receiver[T] {

  val size: Int

  private var data: Seq[Datum[T]] = Seq.empty

  def buffer: Seq[Datum[T]] = data

  def receive(emitter: Emitter[T], datum: Datum[T]) = {
    data = data :+ datum
    data = data.drop(data.size - size)
  }

}

trait CachedEmitter[T] extends Emitter[T] {

  private var cached: Option[Datum[T]] = None

  def last: Option[Datum[T]] = cached

  override def emit(datum: Datum[T]) = {
    cached = Some(datum)
    super.emit(datum)
  }

}

sealed trait Functor[T, R] extends Receiver[T] with Emitter[R]

trait UnbufferedFunctor[T, R] extends Functor[T, R] with CachedEmitter[R] {

  protected def result(datum: Datum[T]): Datum[R]

  def receive(emitter: Emitter[T], datum: Datum[T]) = emit(result(datum))

}

trait BufferedFunctor[T, R] extends Functor[T, R] with CachedEmitter[R] with BufferedReceiver[T] {

  protected def result(data: Seq[Datum[T]]): Datum[R]

  override def receive(emitter: Emitter[T], datum: Datum[T]) = {
    super.receive(emitter, datum)
    emit(result(buffer))
  }

}
