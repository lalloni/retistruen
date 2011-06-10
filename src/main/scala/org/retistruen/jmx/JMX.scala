package org.retistruen.jmx

import java.util.Hashtable
import javax.management.ObjectName.{ getInstance ⇒ ObjectName }
import javax.management.{ Attribute, AttributeList, DynamicMBean, MBeanAttributeInfo, MBeanInfo, StandardMBean }
import org.retistruen.{ Named, Pollable, Source }
import scala.collection.JavaConversions._
import scala.math.ScalaNumericConversions

trait SourceMBean[T] {
  protected val source: Source[T]
  def getName = source.name
  def emit(value: T) = source << value
}

class SourceObject[T](protected val source: Source[T]) extends SourceMBean[T]

trait JMX extends Named {

  protected var structure: Seq[Named]

  private def select[T](implicit m: Manifest[T]): Seq[T] = structure.filter(m.erasure.isInstance(_)).asInstanceOf[Seq[T]]

  private def pollables = select[Pollable[_]]

  private def sources = select[Source[_]]

  object MBean extends DynamicMBean {

    def attributes: Array[MBeanAttributeInfo] =
      pollables.toArray.map(pollable ⇒ new MBeanAttributeInfo(pollable.name, classOf[java.lang.Double].getName, null, true, false, false))

    def getMBeanInfo: MBeanInfo =
      new MBeanInfo(getClass.getName, name, attributes, null, null, null)

    def invoke(method: String, arguments: Array[AnyRef], signature: Array[String]): AnyRef =
      null

    def getAttribute(name: String): Attribute =
      attribute(pollables.find(pollable ⇒ pollable.name == name).get)

    def setAttribute(attribute: Attribute): Unit =
      Unit

    def getAttributes(names: Array[String]): AttributeList =
      new AttributeList(names.map(getAttribute(_)).toList)

    def setAttributes(attributes: AttributeList): AttributeList =
      null

    private def attribute(pollable: Pollable[_]) =
      new Attribute(pollable.name, pollable.poll.getOrElse(null))

  }

  private def table(pairs: (String, String)*) = {
    val ht = new Hashtable[String, String]
    for ((a, b) ← pairs) ht.put(a, b)
    ht
  }

  def registerMBeans = {
    val server = bestMBeanServer("jboss")
    server.registerMBean(
      MBean,
      ObjectName("retistruen", table("model" -> name, "type" -> "model")))
    for (source ← sources)
      server.registerMBean(
        new StandardMBean(new SourceObject(source), classOf[SourceMBean[_]]),
        ObjectName("retistruen", table("model" -> name, "type" -> "source", "name" -> source.name)))
  }

}
