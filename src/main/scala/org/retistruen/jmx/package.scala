package org.retistruen

import javax.management.{ MBeanServer, MBeanServerFactory }
import scala.collection.JavaConversions._
import java.lang.management.ManagementFactory

/**
 * This package contains plugins for registering retistruen instruments on JMX.
 * See [[org.retistruen.jmx.MBeanRegistering]] for futher detail and usage.
 */
package object jmx {

  /** Returns the set of MBeanServer with matching default domain name. */
  def mbeanServers(domain: String): Seq[MBeanServer] =
    MBeanServerFactory.findMBeanServer(null).filter(_.getDefaultDomain == domain)

  /** Returns the first MBeanServer matching default domain name or none if not found. */
  def mbeanServer(domain: String): Option[MBeanServer] = mbeanServers(domain).headOption

  def bestMBeanServer(domain: String): MBeanServer =
    mbeanServer(domain).getOrElse(platformMBeanServer)

  lazy val platformMBeanServer: MBeanServer =
    ManagementFactory.getPlatformMBeanServer
    
}
