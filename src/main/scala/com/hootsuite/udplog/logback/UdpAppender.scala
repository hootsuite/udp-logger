package com.hootsuite.udplog.logback

import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.Layout

import com.hootsuite.dns.SrvResolver
import com.hootsuite.udplog.UdpCannon

/**
 * Appender that fires logging events to a UDP address.
 *
 * This should be configured with a layout in logback XML.
 */
class UdpAppender[T] extends AppenderBase[T] {

  // UDP and SRV support.
  private val cannon = UdpCannon
  private val resolver = new SrvResolver

  // XML configuration support.
  private var configLayout: Layout[T] = null
  def setLayout(layout: Layout[T]) = { configLayout = layout }
  def getLayout = configLayout

  private var configServiceName: String = null
  def setService(name: String) = { configServiceName = name }
  def getService = configServiceName

  // this will only be referenced after config properties
  // are checked by start(), but
  // ensures they are fixed once set.
  private lazy val layout = configLayout
  private lazy val serviceName = configServiceName

  override def start() {
    if (configLayout == null) addError("No layout set for %s" format(name))
    if (configServiceName == null) addError("No service set for %s" format(name))
    super.start()
  }

  def append(event: T) {
    val bytes = layout.doLayout(event).getBytes("UTF-8")
    resolver lookup(serviceName) match {

      case Right((name: String, port: Int)) =>
        val host = name.substring(0, name.length - 1)
//        addInfo("Sending [%s] to %s:%s" format(new String(bytes), host, port))
        cannon(host, port, bytes)

      case Left(error) =>
        addError(error)
    }
  }
}
