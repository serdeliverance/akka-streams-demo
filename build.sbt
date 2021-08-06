import Dependencies._

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github"
ThisBuild / organizationName := "serdeliverance"

lazy val root = (project in file("."))
  .settings(
    name := "akka-streams-demo"
  )

val AkkaVersion     = "2.6.15"
val postgresVersion = "42.2.2"
val circeVersion    = "0.13.0"

libraryDependencies ++= Seq(
  // akka streams
  "com.typesafe.akka"  %% "akka-stream"               % AkkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "3.0.2",
  "ch.qos.logback"     % "logback-classic"            % "1.2.3",
  // circe
  "io.circe" %% "circe-core"           % circeVersion,
  "io.circe" %% "circe-generic"        % circeVersion,
  "io.circe" %% "circe-parser"         % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  // db
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "com.typesafe.slick" %% "slick"          % "3.3.3",
  "org.postgresql"     % "postgresql"      % postgresVersion,
  // misc
  "com.github.javafaker" % "javafaker" % "1.0.2",
  scalaTest              % Test
)
