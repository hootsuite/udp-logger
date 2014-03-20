# UdpLogger

This library provides the capability to log over UDP to services such
as [logstash][logstash] (for further analysis in systems such as
[elastic search][elastic-search]). It provides appenders for logback and log4j, and a
caching DNS client to resolve servers in SRV records.


## Components

The library has the following responsibilities:

* Accepts log events.
* Checks and caches service records to support UDP load balancing of log servers
  is supported (using dnsjava). TTL is respected.
* Sends logged messages over UDP.
* Supports (and requires) a pluggable formatter. At HootSuite we use a structured JSON format.

Since UDP is used, delivery is not assured.


## Configuration

Sample logback.xml fragment:

    <appender name="logstash" class="com.hootsuite.udplog.logback.UdpAppender">
      <service>_logstash._udp.hootsuitemedia.com</service>
      <layout class="com.hootsuite.stump.StumpFormatter"/>
      <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
          <level>DEBUG</level>
      </filter>
      </appender>

    ...

    <root level="DEBUG">
      <appender-ref ref="disk" />
      <appender-ref ref="logstash" />
    </root>

Sample log4j-config.xml fragment:

    <appender name="logstash" class="com.hootsuite.udplog.log4j.UdpAppender">
      <param name="service" value="_logstash.udp.company.com" />
      <layout class="{layout of your choice}"/>
    </appender>

    ...

    <root>
      <priority value="debug">
      <appender-ref ref="disk" />
      <appender-ref ref="console" />
    </root>
    

You can add `debug="true"` to the root element to check you are sending log
messages correctly, or run `tcpdump -n udp` to watch the datagrams go.


## Installation

Just include udp-logger and [DNS-java][dns-java] jars into your app's classpath or build-time dependencies, and
update your logger's configuration.


## Run

UdpLogger also works as a standalone program to test resolution and
use of logstash servers. You may run it like this:

    java -cp scala-library.jar:udp-logger_2.9.1-0.2.jar:dnsjava-2.1.6.jar com.hootsuite.udplog.Main _logstash._udp.company.com "ECHO TEST"

After running, you should be able to see the log message either in
your log montoring system (e.g. [kibana][kibana]), or by running `tcpdump -n
udp and port 55555` on the target server.

If the elastic search server didn't show the message, check your hosts can communicate over UDP.

1. ssh to the client server
2. ssh to the logstash server
3. `sudo tcpdump -n udp and host 204.15.173.160 and port 55555` on logstash
4. `echo 'test' | nc -u logstash1.hootsuite.com 55555` on client
5. check if logstash got a message which length is 4



[logstash]: http://logstash.net/
[elastic-search]: http://elasticsearch.org/
[kibana]: http://www.elasticsearch.org/overview/kibana/
[udp-logger]: http://localhost
[dns-java]: http://www.dnsjava.org/download/
