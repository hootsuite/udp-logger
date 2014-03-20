package com.hootsuite.udplog.log4j

import com.hootsuite.dns.SrvResolver
import com.hootsuite.udplog.UdpCannon
import org.apache.log4j.{AppenderSkeleton}
import org.apache.log4j.spi.LoggingEvent

class UdpAppender extends AppenderSkeleton {
  private val resolver = new SrvResolver
  private var optService: Option[String] = None
  def setService(s: String) { optService = Some(s) }
  def getService: String = {
    optService.getOrElse {
      "-undefined-"
    }
  }

  override def append(event: LoggingEvent) {
    val bytes = this.getLayout.format(event).getBytes("UTF-8")

    optService map { service =>
      resolver lookup(service) match {
        case Right((name: String, port: Int)) =>
          val host = name.substring(0, name.length - 1)
          UdpCannon(host, port, bytes)

        case Left(error) =>
          // ...
      }
    }
  }

  override def close() = {}
  override def requiresLayout() = true
}
