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

# Project Meta
VERSION_NUMBER = "0.1"
GROUP = "org.retistruen"
COPYRIGHT = "The retistruen Team"

# Project
desc "The retistruen Project"
define "retistruen" do

  project.version = VERSION_NUMBER
  project.group = GROUP

  manifest["Implementation-Vendor"] = COPYRIGHT

  resources

  compile.with JODA_TIME, JUNG, COLLECTIONS_GENERIC, COMMONS_MATH
  compile.using :debug => false

  test.resources
  test.using :customscalatest

end
