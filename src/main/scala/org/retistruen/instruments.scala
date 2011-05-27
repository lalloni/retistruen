package org.retistruen

class SourceEmitter[@specialized T](val name: String) extends CachingEmitter[T]

class Variable[@specialized T](val name: String) extends SimpleFunctor[T, T] {
  override protected def operate(datum: Datum[T]): Datum[T] = datum
}

class SlidingMax[@specialized O: Ordering](val name: String, val size: Int) extends SequenceFunctor[O, O] {
  override protected def operate(data: Seq[Datum[O]]): Datum[O] = Datum(data.map(_.value).max)
}

class AbsoluteMax[@specialized O: Ordering](val name: String) extends SimpleFunctor[O, O] {
  val o = implicitly[Ordering[O]]
  import o._
  override protected def operate(received: Datum[O]): Datum[O] = last match {
    case Some(previous) ⇒ if (received.value > previous.value) received.refresh else previous.refresh
    case None           ⇒ received.refresh
  }
}

class RecordingReceiver[@specialized T](val name: String, val capacity: Option[Int] = None) extends Receiver[T] {
  private var buffer: Seq[Datum[T]] = Seq.empty
  def data = buffer
  def receive(emitter: Emitter[T], datum: Datum[T]) = {
    buffer = buffer :+ datum
    for (c ← capacity) buffer = buffer.drop(buffer.size - c)
  }
  def clear = buffer = Seq.empty
}
