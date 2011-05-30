package org.retistruen.instrument

import org.retistruen.CachingEmitter

/** A simple [[org.retistruen.Emitter]] that to be used as entry point */
class SourceEmitter[@specialized T](val name: String) extends CachingEmitter[T]
