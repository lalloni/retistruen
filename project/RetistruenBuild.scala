import sbt._
import Keys._

class RetistruenBuild extends Build {

  lazy val retistruen = Project(
    id = "retistruen",
    base = file("."),
    settings = Defaults.defaultSettings ++
      Seq(
        libraryDependencies <++= scalaVersion { sv: String => Seq(Dependency.ScalaTest(sv), Dependency.Akka(sv)) }
      )
  )

  object Dependency {

    lazy val Version = """(\d+)\.(\d+)\.(\d+)([.-](\d+))?""".r

    def ScalaTest(scalaVersion: String) =
      "org.scalatest" %% "scalatest" % (scalaVersion match {
        case Version("2", "8", "0") ⇒ "1.3.1.RC2"
        case Version("2", "8", "1") ⇒ "1.5.1"
        case _                      ⇒ "1.6.1"
      }) % "test"

    def Akka(scalaVersion: String) =
      "se.scalablesolutions.akka" % "akka-actor" % (scalaVersion match {
        case Version("2", "8") ⇒ "1.0"
        case Version("2", "9") ⇒ "1.2"
      })

  }

}

