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

#Release.message = lambda { |version| "Tagged version #{version}" }
Release.next_version = lambda do |version| 
  v = version.gsub(/-SNAPSHOT$/, "").split(/\./)
  v[-1] = v[-1].to_i + 1
  v.join(".") 
end  
