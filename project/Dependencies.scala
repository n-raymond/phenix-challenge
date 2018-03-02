import sbt._

object Dependencies {

    lazy val get = List(

        /* Tests */
        "org.scalatest" %% "scalatest" % "3.0.4" % Test,
        "org.scalamock" %% "scalamock" % "4.1.0" % Test,

        /* Logging */
        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
    )
}
