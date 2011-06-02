import sbt._

class RetistruenProject(info: ProjectInfo) extends DefaultProject(info) {

  val MavenCentral = "Central Maven Repository" at "http://repo1.maven.org/maven2"
  val ScalaTools = "Scala Tools Releases Repository" at "http://scala-tools.org/repo-releases"

  val JodaTime = "joda-time" % "joda-time" % "1.6.2"

  val JungAPI = "net.sf.jung" % "jung-api" % "2.0"
  val JungGraph = "net.sf.jung" % "jung-graph-impl" % "2.0"
  val JungAlgorithms = "net.sf.jung" % "jung-algorithms" % "2.0"
  val JungVisualization = "net.sf.jung" % "jung-visualization" % "2.0"

  val CollectionsGeneric = "net.sourceforge.collections" % "collections-generic" % "4.01"

  val CommonsMath = "org.apache.commons" % "commons-math" % "2.2"

  val ScalaTest = "org.scalatest" %% "scalatest" % "1.4.1"

}