package org.retistruen.test

import org.mockito.ArgumentMatcher

object CustomMockitoMatchers {

  class ArgumentVerifiesMatcher[T](f: T ⇒ Boolean) extends ArgumentMatcher[T] {
    override def matches(arg: Any) = f(arg.asInstanceOf[T])
  }

  def verifies[T](f: T ⇒ Boolean): ArgumentMatcher[T] = new ArgumentVerifiesMatcher(f)

}
