name := "UdpLogger"

version := "0.1"

scalaVersion := "2.10.0"

organization := "com.hootsuite"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions += "-feature"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.0.0",
  "org.scalatest" %% "scalatest" % "1.9.1",
  "dnsjava" % "dnsjava" % "2.1.1",
  "ch.qos.logback" % "logback-core" % "1.0.11",
  "ch.qos.logback" % "logback-classic" % "1.0.11"
)
