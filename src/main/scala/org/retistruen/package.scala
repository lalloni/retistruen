package org.retistruen

import java.util.Calendar
import org.joda.time.Instant
import org.joda.time.ReadableInstant
import scala.collection.immutable.Queue
import scala.actors.Actor

case class Tag[@specialized T](name: String, value: T) {
  override def toString = "#[%s:%s]" format (name, value)
}

case class Tagging(tags: Set[Tag[_]]) {
  val tagsByName = tags.groupBy(_.name)
  validateUniqueness(tagsByName)
  private def validateUniqueness(index: Map[String, Set[Tag[_]]]) = {
    val repeated = index.values.filter(_.size > 1)
    if (!repeated.isEmpty)
      throw sys.error("Duplicate tags {" + repeated.mkString(", ") + "}")
  }
}

case class Datum[@specialized T](value: T, created: ReadableInstant = new Instant, tagging: Option[Tagging] = None) {
  def this(value: T, tagging: Option[Tagging]) = this(value, new Instant, tagging)
  def refresh = new Datum(value, new Instant, tagging)
}

trait Named {
  val name: String
  override def toString = "%s[%s]" format (getClass.getSimpleName, name)
}

trait Receiver[T] extends Named {
  def receive(emitter: Emitter[T], datum: Datum[T])
}

trait Emitter[T] extends Named {

  private var receivers: Set[Receiver[T]] = Set.empty

  def registered: Set[Receiver[T]] = receivers
  def register(rec: Receiver[T]): Unit =
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

trait SlidingReceiver[T] extends Receiver[T] {

  val size: Int

  private var data: Seq[Datum[T]] = Seq.empty

  def window: Seq[Datum[T]] = data

  def receive(emitter: Emitter[T], datum: Datum[T]) = {
    data = data :+ datum
    data = data.drop(data.size - size)
  }

}

trait CachingEmitter[T] extends Emitter[T] {

  private var cached: Option[Datum[T]] = None

  def last: Option[Datum[T]] = cached

  override def emit(datum: Datum[T]) = {
    cached = Some(datum)
    super.emit(datum)
  }

}

sealed trait Functor[T, R] extends Receiver[T] with CachingEmitter[R]

trait SimpleFunctor[T, R] extends Functor[T, R] {

  protected def operate(datum: Datum[T]): Datum[R]

  def receive(emitter: Emitter[T], datum: Datum[T]) = emit(operate(datum))

}

trait SequenceFunctor[T, R] extends Functor[T, R] with SlidingReceiver[T] {

  protected def operate(data: Seq[Datum[T]]): Datum[R]

  override def receive(emitter: Emitter[T], datum: Datum[T]) = {
    super.receive(emitter, datum)
    emit(operate(window))
  }

}
