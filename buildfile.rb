# Scala
require 'buildr/scala'

# Compile Dependencies
JODA_TIME = "joda-time:joda-time:jar:1.6.2"

# Test Dependencies
SCALATEST = transitive("org.scalatest:scalatest_#{Scala.version}:jar:1.4.1")


# Repositories
repositories.remote << "http://www.ibiblio.org/maven2/"

# Project Meta
VERSION_NUMBER = "0.1"
GROUP = "retistruen"
COPYRIGHT = "The retistruen Team"

# Project
desc "The retistruen Project"
define "retistruen" do
  
  project.version = VERSION_NUMBER
  project.group = GROUP

  manifest["Implementation-Vendor"] = COPYRIGHT

  resources
  
  compile.with(JODA_TIME)
  compile.using(:debug=>false)

  test.resources
  test.using(:scalatest)

end
