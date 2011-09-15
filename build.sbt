
/* ==== General ============================================================= */

name := "retistruen"

version := "0.4-SNAPSHOT"

organization := "plalloni"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.1", "2.8.1")

resolvers += "Akka Repository" at "http://akka.io/repository"

/* ==== Dependencies ======================================================== */

libraryDependencies ++= Seq(
	"joda-time" % "joda-time" % "1.6.2",
	"se.scalablesolutions.akka" % "akka-actor" % "1.1.3",
	"org.clapper" %% "grizzled-slf4j" % "0.6.6",
	"org.slf4j" % "slf4j-api" % "1.6.2") ++ 
	Seq(
		"jung-api",
		"jung-graph-impl", 
		"jung-algorithms", 
		"jung-visualization")
			.map("net.sf.jung" % _ % "2.0")

/* ==== Test Dependencies =================================================== */

libraryDependencies ++= (
    Seq("org.scalatest" %% "scalatest" % "1.6.1", 
		"org.mockito" % "mockito-core" % "1.8.5") ++
	Seq("logback-classic", "logback-core")
        .map("ch.qos.logback" % _ % "0.9.29")
).map(_ % "test") 

/* ==== Publishing ========================================================== */

publishMavenStyle := true

//publishTo := Some(Resolver.file("Test Repository", file("target/repository")))

publishTo <<= version { (version: String) ⇒
  if(version endsWith "-SNAPSHOT") 
    Some("DIT External Snapshots" at "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external-snapshots")
  else 
    Some("DIT External Releases" at "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external")
}
