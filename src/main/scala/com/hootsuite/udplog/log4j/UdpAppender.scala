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
