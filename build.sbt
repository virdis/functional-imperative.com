name := """newBlog"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.sandinh" % "play-hikaricp_2.11" % "1.7.1",
  "com.typesafe.slick" %% "slick" % "3.0.0-M1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "mysql" % "mysql-connector-java" % "5.1.34",
   "org.scalaz" %% "scalaz-core" % "7.1.0"
)
