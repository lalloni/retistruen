package org.retistruen

import org.joda.time.{ Instant, ReadableInstant }

/** A Tag is a named value that can be applied through [[org.retistruen.Tagging]]
 *  to objects to be used as data for drilling down into a set for analysis purposes.
 *  @tparam T the type of the optional [[org.retistruen.Tag]] value */
case class Tag[@specialized T](name: String, value: Option[T] = None) extends Named {
  override def toString = "#[%s%s]" format (name, value match {
    case Some(v) ⇒ ":%s" format v
    case None    ⇒ ""
  })
}

/** Represents a tagging over an object */
case class Tagging(tags: Set[Tag[_]]) {

  /** Returns the values grouped by name */
  lazy val grouped: Map[String, Set[Option[_]]] = tags.groupBy(_.name).mapValues(_.map(_.value))

  def merge(other: Tagging) = Tagging(tags ++ other.tags)

  override def toString = tags.mkString("#{", ",", "}")

}

/** Something that has a [[org.retistruen.datum.Tagging]] */
trait Tagged {
  def tagging: Option[Tagging]
}

/** Represents a value with its creation time and an optional [[org.retistruen.Tagging]]
 *  @tparam T The type of the value represented by this [[org.retistruen.Datum]] */
case class Datum[T](value: T, created: ReadableInstant = new Instant, tagging: Option[Tagging] = None) extends Tagged {

  def this(value: T, tagging: Option[Tagging]) = this(value, new Instant, tagging)

  /** Returns a new copy of this [[org.retistruen.Datum]] with a new timestamp */
  def fresh = new Datum(value, new Instant, tagging)

  /** Returns a new copy of this [[org.retistruen.Datum]] with a new timestamp and value (keeps tagging) */
  def freshWith[R](value: R) = new Datum[R](value, new Instant, tagging)

}

object Datum {
  def apply[T](value: T) = new Datum(value, None)
  def apply[T](value: T, tagging: Option[Tagging]) = new Datum(value, tagging)
}

