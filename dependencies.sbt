resolvers += "Akka Releases" at "http://repo.akka.io/releases"

libraryDependencies ++= Seq("joda-time" % "joda-time" % "1.6.2", 
                            "org.slf4j" % "slf4j-api" % "1.6.2")

libraryDependencies ++= Seq("jung-api", 
                            "jung-graph-impl", 
                            "jung-algorithms", 
                            "jung-visualization")
                          .map("net.sf.jung" % _ % "2.0" % "optional")

libraryDependencies ++= Seq("org.mockito" % "mockito-core" % "1.8.5" % "test", 
                            "junit" % "junit" % "4.9" % "test",
                            "ch.qos.logback" % "logback-classic" % "0.9.29" % "test")

