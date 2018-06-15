import Dependencies._

lazy val root = (project in file(".")).
    settings(
        organization := "com.example",
        scalaVersion := "2.12.4",
        version      := "0.1.0-SNAPSHOT",
        name := "phenix-challenge",
        libraryDependencies ++= get
    )

enablePlugins(JavaAppPackaging)
