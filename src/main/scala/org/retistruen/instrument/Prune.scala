package org.retistruen.instrument

import org.joda.time.{ Instant, ReadableDuration, ReadablePeriod }
import org.retistruen._

object Prune {

  def bySize[T](size: Int): Seq[Datum[T]] ⇒ Seq[Datum[T]] =
    { data ⇒ data.drop(data.size - size) }

  /* Assumes the datums are sorted 
   * chronologically by creation instant */
  def byDuration[T](duration: ReadableDuration): Seq[Datum[T]] ⇒ Seq[Datum[T]] = {
    val limit = new Instant minus duration
    return { data ⇒ data dropWhile (_.created isBefore limit) }
  }

  def byPeriod[T](period: ReadablePeriod): Seq[Datum[T]] ⇒ Seq[Datum[T]] =
    byDuration(period.toPeriod.toStandardDuration)

}
