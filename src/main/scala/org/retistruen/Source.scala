/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 30/05/2011 17:19:32
 */
package org.retistruen

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 * @since 30/05/2011 17:19:32
 */
trait Source[T] extends Emitter[T] {

  /** $Emits */
  def <<(datum: Datum[T]) = emit(datum)

  /** $EmitsValue */
  def <<(some: T) = emit(some)

  /** $EmitsValue */
  def <<<(some: T*) = some foreach (emit _)

}
