package org.retistruen.jmx

import java.util.Hashtable
import javax.management.ObjectName.{ getInstance ⇒ ObjectName }
import javax.management.{ Attribute, AttributeList, DynamicMBean, MBeanAttributeInfo, MBeanInfo, StandardMBean }
import org.retistruen.building.BuildingInfrastructure
import org.retistruen.{ Named, Pollable, Source }
import scala.collection.JavaConversions._
import scala.math.ScalaNumericConversions
import org.retistruen.OpenSource

trait SourceMBean[T] {
  protected val source: OpenSource[T]
  def getName = source.name
  def emit(value: String) = source << value
}

class SourceObject[T](protected val source: OpenSource[T]) extends SourceMBean[T]

trait JMX extends Named {

  this: BuildingInfrastructure ⇒

  private def select[T](implicit m: Manifest[T]): Seq[T] = components
    .filter(m.erasure.isInstance(_))
    .map(_.asInstanceOf[T])

  object MBean extends DynamicMBean {

    def attributes: Array[MBeanAttributeInfo] =
      select[Pollable[_]].toArray.map(pollable ⇒ new MBeanAttributeInfo(pollable.name, classOf[java.lang.Double].getName, null, true, false, false))

    def getMBeanInfo: MBeanInfo =
      new MBeanInfo(getClass.getName, name, attributes, null, null, null)

    def invoke(method: String, arguments: Array[AnyRef], signature: Array[String]): AnyRef =
      null

    def getAttribute(name: String): Attribute =
      attribute(select[Pollable[_]].find(pollable ⇒ pollable.name == name).get)

    def setAttribute(attribute: Attribute): Unit =
      Unit

    def getAttributes(names: Array[String]): AttributeList =
      new AttributeList(names.map(getAttribute(_)).toList)

    def setAttributes(attributes: AttributeList): AttributeList =
      null

    private def attribute(pollable: Pollable[_]) =
      new Attribute(pollable.name, pollable.poll.getOrElse(null))

  }

  private def table(pairs: List[(String, String)]) = {
    val ht = new Hashtable[String, String]
    for ((a, b) ← pairs.reverse) ht.put(a, b)
    ht
  }

  val jmxRegistrationDomain = "org.retistruen"

  private def on(key: (String, String)*) =
    ObjectName(jmxRegistrationDomain, table(("model" -> name) :: key.toList))

  def registerMBeans = {
    val server = bestMBeanServer("jboss")
    server.registerMBean(MBean,
      on("type" -> "model", "name" -> name))
    for (source ← select[OpenSource[_]])
      server.registerMBean(new StandardMBean(new SourceObject(source), classOf[SourceMBean[_]]),
        on("type" -> "source", "name" -> source.name))
  }

  def unregisterMBeans = {
    val server = bestMBeanServer("jboss")
    server.unregisterMBean(on("type" -> "model", "name" -> name))
    for (source ← select[OpenSource[_]])
      server.unregisterMBean(on("type" -> "source", "name" -> source.name))
  }

}
