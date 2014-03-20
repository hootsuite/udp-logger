name := "udp-logger"

version := "0.2"

scalaVersion := "2.10.3"

organization := "com.hootsuite"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

publishTo := Some(Resolver.file("HootSuite repo", file("/var/www/maven-development")))

libraryDependencies ++= Seq(
  "com.typesafe"    % "config"          % "1.0.0",
  "org.scalatest"   %% "scalatest"      % "2.0" % "test",
  "dnsjava"         % "dnsjava"         % "2.1.6",
  "ch.qos.logback"  % "logback-core"    % "1.0.13",
  "ch.qos.logback"  % "logback-classic" % "1.0.13",
  "log4j"           % "log4j"           % "1.2.17"
)
