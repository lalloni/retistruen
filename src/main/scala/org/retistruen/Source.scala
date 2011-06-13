package org.retistruen

trait Source[T] extends Emitter[T] {

  /** $Emits */
  def <<(datum: Datum[T]) = emit(datum)

  /** $EmitsValue */
  def <<(some: T) = emit(some)

  /** $EmitsValue */
  def <<<(some: T*) = some foreach (emit _)

}
