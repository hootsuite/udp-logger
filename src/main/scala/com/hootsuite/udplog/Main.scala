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

import com.hootsuite.dns.SrvResolver

object Main extends App {

  override def main(args: Array[String]) {

    val resolver = new SrvResolver
    val cannon = UdpCannon

    if (args.size != 2) {
      usage()
      exit
    }

    val Array(serviceName, testMsg) = args

    val result = resolver.lookup(serviceName)

    println("result of resolving %s: %s" format(serviceName, result))

    result match {
      case Right((name: String, port: Int)) =>
        val host = name.substring(0, name.length - 1)
        println("Sending %s to %s:%s" format(testMsg, host, port))
        cannon(host, port, testMsg.getBytes("UTF-8"))

      case Left(error) =>
        println("lookup failed with: %s" format error)
    }
  }

  def usage() {
    println("""
      |  UDP test
      |
      |  Usage: java -jar logback-udp.jar <service name> <test msg>
      |
      |  where <service name> is a logback UDP service name, e.g.  _logback._udp.hootsuite.com
      |  and <test msg> is a test JSON message.
      """ stripMargin('|'))
  }
}
