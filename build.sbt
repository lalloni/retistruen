name := "retistruen"

version := "0.3-SNAPSHOT"

organization := "org.retistruen"

scalaVersion := "2.9.0-1"

libraryDependencies += "joda-time" % "joda-time" % "1.6.2"

libraryDependencies ++= 
	Seq("jung-api", "jung-graph-impl", "jung-algorithms", "jung-visualization")
		.map("net.sf.jung" % _ % "2.0")

libraryDependencies ++= 
	Seq("org.scalatest" % "scalatest_2.9.0" % "1.6.1", 
	    "org.mockito" % "mockito-core" % "1.8.5")
			.map(_ % "test")

publishMavenStyle := true

//publishTo := Some(Resolver.file("Test Repository", "target/repository"))
