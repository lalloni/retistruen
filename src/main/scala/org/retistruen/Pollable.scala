package org.retistruen

trait Pollable[T] extends Named {

  def poll: Option[T]

}
