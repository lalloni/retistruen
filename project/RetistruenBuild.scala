import sbt._
import Keys._

object RetistruenBuild extends Build {

  lazy val retistruen = Project(
    id = "retistruen",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      libraryDependencies <++= scalaVersion {
        sv: String => Seq(Dep.ScalaTest(sv), Dep.GrizzledSlf4j(sv), Dep.Akka(sv))
      }))

  object Dep {

    lazy val Version = """(\d+)\.(\d+)(?:\.(\d+)(?:[.-](\d+))?)?""".r

    def parse(version: String) = {
      val Version(a, b, c, d) = version
      (a :: b :: c :: d :: Nil) filter (_ != null) map (_.toInt)
    }

    def ScalaTest(scalaVersion: String) =
      "org.scalatest" %% "scalatest" % (parse(scalaVersion) match {
        case _ => "1.9.2"
      }) % "test"

    def GrizzledSlf4j(scalaVersion: String) =
      "org.clapper" %% "grizzled-slf4j" % (parse(scalaVersion) match {
        case _ => "1.0.1"
      })

    def Akka(scalaVersion: String) =
      "se.scalablesolutions.akka" % "akka-actor" % (parse(scalaVersion) match {
        case _ => "1.2"
      })

  }

}
