package org.retistruen

sealed trait Functor[T, R] extends Receiver[T] with Emitter[R]

trait SimpleFunctor[T, R] extends Functor[T, R] with CachingEmitter[R] {

  protected def operate(datum: Datum[T]): Datum[R]

  def receive(emitter: Emitter[T], datum: Datum[T]) = emit(operate(datum))

}

trait SlidingFunctor[T, R] extends Functor[T, R] with SlidingReceiver[T] with CachingEmitter[R] {

  protected def operate(data: Seq[Datum[T]]): Datum[R]

  override def receive(emitter: Emitter[T], datum: Datum[T]) = {
    super.receive(emitter, datum)
    emit(operate(window))
  }

}

trait SlidingCollectorFunctor[T] extends SlidingFunctor[T, Seq[Datum[T]]] {

  protected def operate(data: Seq[Datum[T]]): Datum[Seq[Datum[T]]] =
    Datum(data)

}
