package com.hootsuite.dns

import org.xbill.DNS._

/**
 * Provides name resolution of services through SRV records.
 * The DNS server used is host dependent.
 */
class SrvResolver {

  lazy val cache = new Cache

  /**
   * Performs lookup of a service name (e.g. _logstash._udp.example.com) and
   * returns the hostname. This will return cached entries if their TTL has
   * not passed.
   *
   * If the lookup returns multiple records, the first is used.
   *
   * Blocking.
   * @throws TextParseException if the given serviceName is invalid.
   */
  def lookup(serviceName: String, cache: Cache = this.cache): Either[String,(String, Int)] = {

      val lookup = new Lookup(serviceName, Type.SRV)
      lookup.setCache(cache)

      val result = for {
        results <- Option(lookup.run) // may return null, empty array, array
        record  <- results.headOption
      } yield record match {
        case srv: SRVRecord => srv.getTarget.toString -> srv.getPort
        case _ => throw new IllegalArgumentException("Non-SRV record returned for request")
      }

      result.toRight(lookup.getErrorString)
  }

  /**
   * Performs a clean lookup (i.e. without referring to the cache) of the given
   * service name.
   *
   * @see #lookup(String)
   * @throws TextParseException if the given serviceName is invalid.
   */
  def forceLookup(serviceName: String): Either[String,(String, Int)] = lookup(serviceName, null)
}
