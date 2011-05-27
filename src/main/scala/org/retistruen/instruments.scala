package org.retistruen

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
