package org.retistruen

/** Contains traits and classes for building [[org.retistruen.Model]] instances idiomatically */
package object building {

  private[building] def receiverName(emitter: Emitter[_], name: String, args: Any*): String = {
    val base = emitter.name + "." + name
    if (args.isEmpty) base else base + args.mkString("(", ",", ")")
  }

}
