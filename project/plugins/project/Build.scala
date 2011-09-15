import sbt._ 
import Keys._

object Plugins extends Build {
  
  lazy val root = Project("root", file(".")) dependsOn(sbteclipsify)
  
  lazy val sbteclipsify = uri("git://github.com/musk/SbtEclipsify.git")
  
}
