ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "scala_project"
  )

val AkkaVersion = "2.8.2"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.6" % Runtime
libraryDependencies += "com.lihaoyi" %% "upickle" % "3.0.0"
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.9.1"

