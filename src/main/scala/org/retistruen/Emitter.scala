package org.retistruen

import org.joda.time.Instant

/** Emits values as instances of $Datum to the set of registered $Receiver
 *  @tparam T the type of the emitted values
 *  @define Receiver [[org.retistruen.Receiver]]
 *  @define Emitter [[org.retistruen.Emitter]]
 *  @define Datum [[org.retistruen.Datum]]
 *  @define Emits Emits the value wrapped in the given $Datum to all registered $Receiver
 *  @define Registers Registers the $Receiver to be sent values emitted by this Emitter.
 *  @define EmitsValue Emits the value provided wrapped in a new $Datum */
trait Emitter[T] extends Named {

  private var receivers: Set[Receiver[T]] = Set.empty

  /** Returns the set of $Receiver registered with this Emitter to receive emitted $Datum */
  def registered: Set[Receiver[T]] = receivers

  /** $Registers */
  def register(rec: Receiver[T]): Unit = {
    receivers = receivers + rec
    rec.registered(this)
  }

  /** $Registers */
  def >>(other: Receiver[T]): Unit = register(other)

  /** $Registers */
  def >>>(others: Receiver[T]*): Unit = others foreach register

  /** $Registers Also returns the {{$Receiver with Emitter}} as Emitter to
   *  support a fluent registration idiom like:
   *  {{{
   *  emitter1 >> emitterreceiver1 >> emitterreceiver2 >> receiver
   *  }}}
   */
  def >>(other: Receiver[T] with Emitter[T]): Emitter[T] = {
    register(other)
    other
  }

  /** $Emits */
  protected def emit(datum: Datum[T]): Unit =
    receivers.foreach { receiver ⇒
      try receiver.receive(this, datum)
      catch {
        case e ⇒ e.printStackTrace
      }
    }

  /** $EmitsValue */
  protected def emit(some: T): Unit =
    emit(Datum(some, new Instant))

}

/** $Emitter that keeps the last emitted $Datum cached for further access */
trait CachingEmitter[T] extends Emitter[T] with Pollable[T] {

  private var cached: Option[Datum[T]] = None

  /** Returns the last $Datum emitted by this $Emitter or None if none emitted yet */
  def last: Option[Datum[T]] = cached

  /** Returns the value contained in the last $Datum emitted by this $Emitter or None if none emitted yet */
  def lastValue: Option[T] = last.map(_.value)

  override protected def emit(datum: Datum[T]) = {
    cached = Some(datum)
    super.emit(datum)
  }

  def poll = lastValue

}
