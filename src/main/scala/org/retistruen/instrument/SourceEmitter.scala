package org.retistruen.instrument

import org.retistruen.CachingEmitter
import org.retistruen.Source
import org.retistruen.OpenSource
import org.retistruen.ReadableFromString

/** A simple [[org.retistruen.Source]] [[org.retistruen.Emitter]] to be used as entry point */
class SourceEmitter[T](val name: String)
  extends Source[T] with CachingEmitter[T]

/** An [[org.retistruen.OpenSource]] [[org.retistruen.Emitter]] to be used as entry point */
class OpenSourceEmitter[T: ReadableFromString](val name: String)
  extends OpenSource[T] with CachingEmitter[T]
