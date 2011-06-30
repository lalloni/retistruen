package org.retistruen.survey

import java.util.concurrent.atomic.AtomicInteger
import org.retistruen._
import org.joda.time.Instant

/**
 * Implements a survey for sending code execution concurrency level to a
 * retistruen Source.
 *
 * This implementation counts the number of concurrent entries into a code block
 * and sending a the current count before executing the block.
 *
 * You instantiate one of this passing in the Source[Int] you want this survey
 * to send its findings:
 *
 * {{{
 * val concurrency = new ConcurrencySurvey(source)
 * }}}
 *
 * Aftear you hace one iniatilized, the preferred usage pattern is:
 *
 * {{{
 * concurreny.survey {
 *   // your block of code to monitor for reentrance count
 * }
 * }}}
 *
 * Every time this code is executed the survey will send a concurrency reading
 * to the Source.
 *
 * For conciseness and functional style the block can return a value which
 * will be returned by the "survey" method so you can assign it like in:
 *
 * {{{
 * val result = concurrenvy.survey {
 *   // do something concurrent
 *   2 + 2
 * }
 * }}}
 *
 * That code would return 4 into "result.
 *
 * Alternatively, although more error prone and not recomended, you can
 * implement the monitoring like this:
 *
 * {{{
 * concurrency.enter
 * try {
 *   // your block of code to monitor for reentrancy count
 * } finally concurrency.leave
 * }}}
 *
 * @param code Is the closure to wrap around
 * @tparam Is the returned value type of the closure
 */
class ConcurrencySurvey(target: Source[Int], tagging: Option[Tagging] = None) {

  val counter = new AtomicInteger(0)

  def enter: Unit =
    target << Datum(counter.incrementAndGet, tagging)

  def leave: Unit =
    target << Datum(counter.decrementAndGet, tagging)

  def survey[T](code: ⇒ T): T = {
    enter
    try code
    finally leave
  }

}
