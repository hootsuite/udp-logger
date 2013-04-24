package com.hootsuite.udplog

import java.net.{InetSocketAddress,DatagramPacket,DatagramSocket}
/**
 * A dumb as bricks UDP client.
 */
object UdpCannon {
  def apply(host: String, port: Int, data: Array[Byte]) {
    val address = new InetSocketAddress(host, port)
    val packet = new DatagramPacket(data, data.length, address)

    val socket = new DatagramSocket()
    socket.send(packet)
    socket.close()
  }
}
