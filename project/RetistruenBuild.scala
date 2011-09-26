import sbt._
import Keys._

object RetistruenBuild extends Build {

  lazy val retistruen = Project(
    id = "retistruen",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      libraryDependencies <++= scalaVersion {
        sv: String => Seq(Dep.ScalaTest(sv), Dep.Akka(sv))
      }
    )
  )

  object Dep {

    lazy val Version = """(\d+)\.(\d+)(?:\.(\d+)(?:[.-](\d+))?)?""".r

    def parse(version: String) = {
      val Version(a, b, c, d) = version
      (a :: b :: c :: d :: Nil) filter (_ != null) map (_.toInt)
    }


    def ScalaTest(scalaVersion: String) =
      "org.scalatest" %% "scalatest" % (parse(scalaVersion) match {
        case 2 :: 8 :: 0 :: _ => "1.3.1.RC2"
        case 2 :: 8 :: 1 :: _ => "1.5.1"
        case _ => "1.6.1"
      }) % "test"


    def Akka(scalaVersion: String) =
      "se.scalablesolutions.akka" % "akka-actor" % (parse(scalaVersion) match {
        case 2 :: 8 :: _ => "1.0"
        case _ => "1.2"
      })


  }

}
