require 'buildr/scala'
require 'buildcustomizations'

# Project Meta
THIS_GROUP = "org.retistruen"
THIS_ARTIFACT = "retistruen"
THIS_VERSION = "0.2-SNAPSHOT"

# Repositories
repositories.remote << "http://www.ibiblio.org/maven2/"
repositories.release_to[:url] = THIS_VERSION=~/-SNAPSHOT$/ ? "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external-snapshots" : "http://artifactsddit.afip.gov.ar/nexus/content/repositories/external"

# Compile Dependencies
JODA_TIME = "joda-time:joda-time:jar:1.6.2"
JUNG = group("jung-api", "jung-graph-impl", "jung-algorithms", "jung-visualization", :version => "2.0", :under => "net.sf.jung")
COLLECTIONS_GENERIC = "net.sourceforge.collections:collections-generic:jar:4.01"
COMMONS_MATH = "org.apache.commons:commons-math:jar:2.2"

# Test Dependencies
MOCKITO = transitive("org.mockito:mockito-all:jar:1.8.1")

# Project
desc "The Retistruen Project"
define THIS_ARTIFACT do

  project.version = THIS_VERSION
  project.group = THIS_GROUP

  resources

  compile.with JODA_TIME, JUNG, COLLECTIONS_GENERIC, COMMONS_MATH
  compile.using :debug => false

  test.resources
  test.using :customscalatest
  test.with MOCKITO

  doc.using :scaladoc

  package :jar
  package :sources
  package_as_scaladoc("target/doc")

end
