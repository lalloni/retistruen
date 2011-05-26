require 'buildr/scala'

module Buildr::Scala
  class CustomScalaTest < Buildr::Scala::ScalaTest
    VERSION = "1.4.1"
    class << self
      def dependencies
        ["org.scalatest:scalatest_#{Scala.version}:jar:#{version}"] + Check.dependencies + JMock.dependencies + JUnit.dependencies
      end
    end
  end
end

Buildr::TestFramework << Buildr::Scala::CustomScalaTest
