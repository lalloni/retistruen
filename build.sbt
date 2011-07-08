name := "retistruen"

version := "0.4-SNAPSHOT"

organization := "org.retistruen"

scalaVersion := "2.9.0"

resolvers += "Akka Repository" at "http://akka.io/repository"

libraryDependencies ++= Seq(
	"joda-time" % "joda-time" % "1.6.2",
	"se.scalablesolutions.akka" % "akka-actor" % "1.1.2",
	"org.clapper" %% "grizzled-slf4j" % "0.5",
	"org.slf4j" % "slf4j-api" % "1.6.1") ++ 
	Seq(
		"jung-api", 
		"jung-graph-impl", 
		"jung-algorithms", 
		"jung-visualization")
			.map("net.sf.jung" % _ % "2.0")

libraryDependencies ++= (
    Seq("org.scalatest" %% "scalatest" % "1.6.1", 
		"org.mockito" % "mockito-core" % "1.8.5") ++
	Seq("logback-classic", "logback-core")
        .map("ch.qos.logback" % _ % "0.9.29")
).map(_ % "test") 

publishMavenStyle := true

//publishTo := Some(Resolver.file("Test Repository", file("target/repository")))

publishTo <<= version { (version: String) =>
  if(version endsWith "-SNAPSHOT") 
    Some("DIT External Snapshots" at "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external-snapshots")
  else 
    Some("DIT External Releases" at "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external")
}
