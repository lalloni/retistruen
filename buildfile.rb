require 'buildr/scala'
require 'buildcustomizations'

# Compile Dependencies
JODA_TIME = "joda-time:joda-time:jar:1.6.2"
JUNG = group("jung-api", "jung-graph-impl", "jung-algorithms", "jung-visualization", :version => "2.0", :under => "net.sf.jung")
COLLECTIONS_GENERIC = "net.sourceforge.collections:collections-generic:jar:4.01"
COMMONS_MATH = "org.apache.commons:commons-math:jar:2.2"

# Test Dependencies
SCALATEST = transitive("org.scalatest:scalatest_#{Scala.version}:jar:1.4.1")

# Repositories
repositories.remote << "http://www.ibiblio.org/maven2/"
repositories.release_to[:url] = "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external-snapshots"

# Project Meta
THIS_VERSION = "0.1-SNAPSHOT"
THIS_GROUP = "org.retistruen"

# Project
desc "The Retistruen Project"
define "retistruen" do

  project.version = THIS_VERSION
  project.group = THIS_GROUP

  resources

  compile.with JODA_TIME, JUNG, COLLECTIONS_GENERIC, COMMONS_MATH
  compile.using :debug => false
  
  test.resources
  test.using :customscalatest
  
  doc.using :scaladoc
  
  package :jar
  package :sources
  package_as_scaladoc("target/doc")

end
