import Dependencies._

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github"
ThisBuild / organizationName := "serdeliverance"

lazy val root = (project in file("."))
  .settings(
    name := "akka-streams-demo"
  )

val AkkaVersion = "2.6.14"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  scalaTest           % Test
)
