package org.retistruen

import java.math.BigInteger

/** Type-class for things that can be read from String */
abstract class ReadableFromString[T] {
  def read(s: String): T
}

/** Basic [[org.retistruen.ReadableFromString]] implementations */
object ReadableFromString {
  implicit val DoubleIsReadableFromString = new ReadableFromString[Double] { def read(s: String) = s.toDouble }
  implicit val IntIsReadableFromString = new ReadableFromString[Int] { def read(s: String) = s.toInt }
  implicit val BigDecimalIsReadableFromString = new ReadableFromString[BigDecimal] { def read(s: String) = BigDecimal(s) }
  implicit val BigIntFromString = new ReadableFromString[BigInt] { def read(s: String) = BigInt(s) }
}

abstract class OpenSource[T: ReadableFromString] extends Source[T] {

  val readableFromString = implicitly[ReadableFromString[T]]

  def <<(value: String) = emit(readableFromString.read(value))

}
