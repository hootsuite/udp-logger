package com.hootsuite.udplog.logback

import ch.qos.logback.core.AppenderBase
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent

import com.typesafe.config.ConfigFactory

import com.hootsuite.dns.SrvResolver
import com.hootsuite.udplog.UdpCannon

import util.control.NonFatal


class UdpAppender(var encoder: PatternLayoutEncoder) extends AppenderBase[ILoggingEvent] {

  def UdpAppender() = new UdpAppender(null)

  val config = ConfigFactory.load
  val cannon = UdpCannon
  val resolver = new SrvResolver


  // our encoder is set up to send to this stream.
  // we mutate it, and synchronize access :(
  val stream = new java.io.ByteArrayOutputStream

  val serviceName = "_logback._udp.hootsuite.com"

  override def start {
    if (this.encoder == null) {
      addError("No encoder set for %s" format(name))
    } else try {
      encoder.init(stream)
    } catch {
      case NonFatal(e) => addError("Error during initialization: " format e.getMessage)
    }

  }

  def append(event: ILoggingEvent) {
    val bytes = synchronized {
      encoder.doEncode(event)
      val bytes = stream.toByteArray
      stream.reset
      bytes
    }
    resolver lookup(serviceName) match { // TODO expose to config
      case Right((host: String, port: Int)) =>
        println("Sending [%s] to %s:%s" format(new String(bytes), host, port))
        cannon(host, port, bytes)
      case Left(error) =>
        addError(error)
    }
  }
}
