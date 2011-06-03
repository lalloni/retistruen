package org.retistruen.jmx

import java.util.Hashtable
import javax.management.ObjectName
import org.retistruen.building.Building
import org.retistruen.Named
import scala.collection.JavaConversions._
import javax.management.DynamicMBean
import javax.management.MBeanInfo
import javax.management.StandardMBean
import org.retistruen.CachingEmitter
import com.sun.jmx.mbeanserver.MBeanSupport
import javax.management.AttributeList
import javax.management.Attribute
import scala.reflect.BeanProperty

trait MBeanRegistering extends Building {

  val mbeanDomain: String

  val mbeanServer = bestMBeanServer("jboss")

  override protected def registered[T <: Named](instrument: T): Unit = {
    super.registered(instrument)
    val name = ObjectName.getInstance(mbeanDomain, new Hashtable(Map("type" -> "instrument", "name" -> instrument.name)))
    mbeanServer.registerMBean(new StandardMBean(instrument, classOf[Named], true), name)
  }

}
