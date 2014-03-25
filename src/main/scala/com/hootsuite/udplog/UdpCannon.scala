// ==========================================================================
// Copyright 2014 HootSuite Media, Inc.
// --------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this work except in compliance with the License.
// You may obtain a copy of the License in the LICENSE file, or at:
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ==========================================================================
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
