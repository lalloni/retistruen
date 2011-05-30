package org.retistruen

import org.joda.time.ReadableInstant
import org.joda.time.Instant

/** A Tag is a named value that can be applied through [[org.retistruen.Tagging]]
 *  to objects to be used as data for drilling down into a set for analysis purposes.
 *  @tparam T the type of the optional [[org.retistruen.Tag]] value
 */
case class Tag[@specialized T](name: String, value: Option[T] = None) extends Named {
  override def toString = "#[%s%s]" format (name, value match {
    case Some(v) ⇒ ":%s" format v
    case None    ⇒ ""
  })
}

/** Represents a tagging over an object */
case class Tagging(tags: Set[Tag[_]]) {

  /** Returns the {tags} grouped by name */
  lazy val grouped: Map[String, Set[Tag[_]]] = tags.groupBy(_.name)

  override def toString = tags.mkString("#{", ",", "}")

}

/** Represents a value with its creation time and an optional [[org.retistruen.Tagging]]
 *  @tparam T The type of the value represented by this [[org.retistruen.Datum]]
 */
case class Datum[@specialized T](value: T, created: ReadableInstant = new Instant, tagging: Option[Tagging] = None) {

  def this(value: T, tagging: Option[Tagging]) = this(value, new Instant, tagging)

  /** Returns a new copy of this [[org.retistruen.Datum]] with a new timestamp */
  def refresh = new Datum(value, new Instant, tagging)

}
