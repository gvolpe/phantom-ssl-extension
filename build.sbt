name := """phantom-ssl-extension"""

version := "1.0"

scalaVersion := "2.11.7"

val PhantomVersion = "1.11.0"

resolvers ++= Seq(
  "Typesafe repository releases"     at "http://repo.typesafe.com/typesafe/releases/",
  "Websudos"                         at "https://dl.bintray.com/websudos/oss-releases/"
)

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.6",
  "com.websudos" %% "phantom-dsl" % PhantomVersion,
  "com.websudos" %% "phantom-testkit" % PhantomVersion
)
