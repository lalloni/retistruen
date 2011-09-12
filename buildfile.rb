require 'buildr/scala'
require 'buildcustomizations'

# Project Meta
THIS_GROUP = "org.retistruen"
THIS_ARTIFACT = "retistruen"
THIS_VERSION = "0.5-SNAPSHOT"

# Repositories
repositories.remote << "http://www.ibiblio.org/maven2"
repositories.remote << "http://akka.io/repository"
repositories.release_to[:url] =
if THIS_VERSION =~ /-SNAPSHOT$/
  "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external-snapshots"
else
  "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external"
end

# Compile Dependencies
JODA_TIME = "joda-time:joda-time:jar:1.6.2"
JUNG = group("jung-api", "jung-graph-impl", "jung-algorithms", "jung-visualization", :version => "2.0", :under => "net.sf.jung")
COLLECTIONS_GENERIC = "net.sourceforge.collections:collections-generic:jar:4.01"
COMMONS_MATH = "org.apache.commons:commons-math:jar:2.2"
AKKA = group("akka-actor", :version=>"1.1.2", :under=>"se.scalablesolutions.akka")
GRIZZLED = "org.clapper:grizzled-slf4j_#{Scala.version}:jar:0.5"
SLF4J = "org.slf4j:slf4j-api:jar:1.6.1"

# Test Dependencies
LOGBACK = group("logback-classic", "logback-core", :under => "ch.qos.logback", :version => "0.9.29")

# Project
desc "The Retistruen Project"
define THIS_ARTIFACT do

  project.version = THIS_VERSION
  project.group = THIS_GROUP

  resources

  compile.with JODA_TIME, JUNG, COLLECTIONS_GENERIC, COMMONS_MATH, AKKA, GRIZZLED, SLF4J
  compile.using :debug => false

  test.resources
  test.with LOGBACK
  test.using :customscalatest

  doc.using :scaladoc

  package :jar
  package :sources
  package_as_scaladoc("target/doc")

end
