package org.retistruen.jmx

import java.util.Hashtable
import javax.management.ObjectName.{getInstance => ObjectName}
import javax.management.{Attribute, AttributeList, DynamicMBean, MBeanAttributeInfo, MBeanInfo}
import org.retistruen.{Named, Pollable}
import scala.collection.JavaConversions._
import scala.math.ScalaNumericConversions

trait JMX extends DynamicMBean with Named {

  protected var structure: Seq[Named]

  def pollables: Seq[Pollable[_]] = structure.filter(_.isInstanceOf[Pollable[_]]).asInstanceOf[Seq[Pollable[_]]]

  bestMBeanServer("jboss")
    .registerMBean(this, ObjectName("retistruen", new Hashtable(Map("type" -> "model", "name" -> name))))

  def attributes: Array[MBeanAttributeInfo] =
    pollables
      .toArray
      .map(pollable ⇒ new MBeanAttributeInfo(strip(pollable), classOf[java.lang.Double].getName, null, true, false, false))

  def getMBeanInfo: MBeanInfo =
    new MBeanInfo(getClass.getName, name, attributes, null, null, null)

  def invoke(method: String, arguments: Array[AnyRef], signature: Array[String]): AnyRef = null

  def getAttribute(name: String): Attribute = attribute(pollables.find(pollable ⇒ strip(pollable) == name).get)
  def setAttribute(attribute: Attribute): Unit = {}

  def getAttributes(names: Array[String]): AttributeList = new AttributeList(names.map(getAttribute(_)).toList)
  def setAttributes(attributes: AttributeList): AttributeList = null

  private def attribute(pollable: Pollable[_]) = new Attribute(strip(pollable), value(pollable))
  private def strip(named: Named) = named.name.drop(name.size + 1)
  private def value(pollable: Pollable[_]) =
    pollable.poll.get match {
      case n: ScalaNumericConversions ⇒ n.doubleValue
      case n: Number                  ⇒ n.doubleValue
      case other                      ⇒ other
    }

}
