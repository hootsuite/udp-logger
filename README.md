#UDP Logger

This library provides an SLF4J appender for UDP servers behind service
records. It's designed for use with logstash.  Configuration is performed with
the typesafe library. Support is also provided for DNS caching of service
records with correct TTL handling.

## Components

The library has the following responsibilities:

* Accepts log messages in standard SLF4J format.
* Checks and caches service records so that UDP load balancing of log servers
	is supported (using dnsjava).
* Sends logged messages over UDP.

Since UDP is used, delivery is not assured.

## Configuration

The library requires a service name, for example
`_logstash._udp.example.com`. A SRV record will be sought here.


## Installation

Just drop the slf4j-udp jar into your project's classpath. Configuration
should be accessible to typesafe's `ConfigFactory`.
