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

  def -->(others: Receiver[T]*): Unit = others.foreach(register(_))

  def -->(other: Receiver[T] with Emitter[T]): Emitter[T] = {
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

/**
 * Es un Receiver que maneja un buffer de los últimos "size" Datums recibidos
 */
trait BufferedReceiver[T] extends Receiver[T] {

  val size: Int

  private var data: Queue[Datum[T]] = Queue.empty

  def buffer: Seq[Datum[T]] = data

  override def receive(emitter: Emitter[T], datum: Datum[T]) = {
    if (data.size >= size) {
      val (_, queue) = data.dequeue
      data = queue.enqueue(datum)
    } else {
      data = data.enqueue(datum)
    }
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

class Variable[T](val name: String) extends UnbufferedFunctor[T, T] {

  protected def result(datum: Datum[T]) = datum

}

class BufferedMax[N: Ordering](val name: String, val size: Int) extends BufferedFunctor[N, N] {

  def result(data: Seq[Datum[N]]) = Datum(data.map(_.value).max)

}

class Max[N: Ordering](val name: String) extends UnbufferedFunctor[N, N] {

  val o = implicitly[Ordering[N]]
  import o._

  def result(received: Datum[N]): Datum[N] = last match {
    case Some(previous) ⇒ if (received.value > previous.value) received.fresh else previous.fresh
    case None           ⇒ received.fresh
  }

}

class RecordingReceiver[T](val name: String, val capacity: Option[Int] = None) extends Receiver[T] {

  private var buffer: Seq[Datum[T]] = Seq.empty

  def data = buffer

  def receive(emitter: Emitter[T], datum: Datum[T]) = {
    buffer = buffer :+ datum
    for (c ← capacity) buffer = buffer.drop(buffer.size - c)
  }

  def clear =
    buffer = Seq.empty

}

class SourceEmitter[T](val name: String) extends CachedEmitter[T]

class Model {

  def source[T](name: String): Emitter[T] = new SourceEmitter[T](name)

  trait Builder[T] {
    val source: Emitter[T]
    protected def name(tag: String): String =
      "%s.%s" format (source.name, tag)
  }

  class FactoryBuilder[T](val source: Emitter[T]) extends Builder[T] {
    def |->(): Emitter[T] = source
  }

  protected implicit def sourceToFactoryBuilder[T: Ordering](e: Emitter[T]): FactoryBuilder[T] = new FactoryBuilder[T](e)

  class MaxBuilder[T: Ordering](val source: Emitter[T]) extends Builder[T] {
    def max(size: Int) = new BufferedMax[T](name("max" + size), size)
    def max = new Max[T](name("max"))
  }

  protected implicit def sourceToMaxBuilder[T: Ordering](e: Emitter[T]): MaxBuilder[T] = new MaxBuilder[T](e)

}

class Sensors extends Model {

  val s1 = source[Int]("s1")
  val s2 = source[Int]("s2")

}