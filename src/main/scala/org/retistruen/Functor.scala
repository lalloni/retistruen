package org.retistruen

sealed trait Functor[T, R] extends Receiver[T] with CachingEmitter[R]

trait SimpleFunctor[T, R] extends Functor[T, R] {

  protected def operate(datum: Datum[T]): Datum[R]

  def receive(emitter: Emitter[T], datum: Datum[T]) = emit(operate(datum))

}

trait SequenceFunctor[T, R] extends Functor[T, R] with SlidingWindowReceiver[T] {

  protected def operate(data: Seq[Datum[T]]): Datum[R]

  override def receive(emitter: Emitter[T], datum: Datum[T]) = {
    super.receive(emitter, datum)
    emit(operate(window))
  }

}
