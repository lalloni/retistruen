publishMavenStyle := true

publishTo <<= version { ver => Some(
    Resolver
        .file("Local Repository", file(Path.userHome 
            + "/projects/artifacts/maven-" 
            + (if (ver endsWith "-SNAPSHOT") "snapshots" else "releases")))
        .mavenStyle)}
