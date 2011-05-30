package org.retistruen.instrument

import org.retistruen.CachingEmitter
import org.retistruen.Source

/** A simple [[org.retistruen.Emitter]] that to be used as entry point */
class SourceEmitter[@specialized T](val name: String) extends Source[T] with CachingEmitter[T]
