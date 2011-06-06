package org.retistruen.jmx

import java.util.Hashtable
import javax.management.ObjectName.{ getInstance ⇒ ObjectName }
import javax.management.{ Attribute, AttributeList, DynamicMBean, MBeanAttributeInfo, MBeanInfo }
import org.retistruen.building.Building
import org.retistruen.{ CachingEmitter, Named }
import scala.collection.JavaConversions._

trait MBean extends Building with DynamicMBean {

  bestMBeanServer("jboss").registerMBean(this, ObjectName("org.retistruen", new Hashtable(Map("type" -> "model", "name" -> name))))

  def attributes: Array[MBeanAttributeInfo] =
    structure.map(i ⇒ new MBeanAttributeInfo(i.name, i.getClass.getName, null, true, false, false)).toArray

  def getMBeanInfo: MBeanInfo =
    new MBeanInfo(getClass.getSimpleName, "retistruen model", attributes, null, null, null)

  def invoke(method: String, arguments: Array[AnyRef], signature: Array[String]): AnyRef = null

  def getAttribute(attribute: String): Attribute =
    structure.find(_.name == attribute).map(i ⇒ new Attribute(attribute, i.asInstanceOf[CachingEmitter[_]].last)).get

  def setAttribute(attribute: Attribute): Unit = {}
  def getAttributes(attributes: Array[String]): AttributeList = new AttributeList(attributes.map(getAttribute(_)).toList)
  def setAttributes(attributes: AttributeList): AttributeList = null

}
