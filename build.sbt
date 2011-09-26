
//====== General =============================================================//

name := "retistruen"

version := "0.5-SNAPSHOT"

organization := "plalloni"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0", "2.8.1", "2.8.0")

resolvers += "Akka Repository" at "http://akka.io/repository"

//===== Dependencies =========================================================//

libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "1.6.2",
    "org.clapper" %% "grizzled-slf4j" % "0.6.6",
    "org.slf4j" % "slf4j-api" % "1.6.2") ++ 
    Seq("jung-api",
        "jung-graph-impl", 
        "jung-algorithms", 
        "jung-visualization")
            .map("net.sf.jung" % _ % "2.0" % "optional")

//===== Test Dependencies ====================================================//

libraryDependencies ++= 
    Seq("org.mockito" % "mockito-core" % "1.8.5" % "test",
        "junit" % "junit" % "4.9" % "test",
        "ch.qos.logback" % "logback-classic" % "0.9.29" % "test")

// ==== Publishing ========================================================== //

publishMavenStyle := true

publishTo <<= version { ver => Some(
    Resolver
        .file("Local Repository", file(Path.userHome 
            + "/projects/artifacts/maven-" 
            + (if (ver endsWith "-SNAPSHOT") "snapshots" else "releases")))
        .mavenStyle)}
