package org.retistruen

/** Common trait for every object which has a name */
trait Named {

  val name: String

  override def toString = name

}
