resolvers += "Akka Releases" at "http://repo.akka.io/releases"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "0.6.0"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.0.1"

libraryDependencies ++= Seq(
    "jung-api",
    "jung-graph-impl",
    "jung-algorithms",
    "jung-visualization"
) map ("net.sf.jung" % _ % "2.0" % "optional")

libraryDependencies ++= Seq(
    "org.mockito" % "mockito-core" % "1.8.5",
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "org.scalatest" %% "scalatest" % "1.9.2"
) map (_ % "test")
