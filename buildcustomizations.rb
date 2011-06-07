require 'buildr/scala'

module Buildr::Scala
  
  module Mockito
    VERSION = '1.8.1'
    class << self
      def version
        Buildr.settings.build['mockito'] || VERSION
      end
      def classifier
        Buildr.settings.build['mockito.classifier'] || ""
      end
      def artifact
        Buildr.settings.build['mockito.artifact'] || "mockito-core"
      end
      def dependencies
        (version =~ /:/) ? [version] : ["org.mockito:#{artifact}:jar:#{classifier}:#{version}", "org.hamcrest:hamcrest-core:jar:1.3.RC2", "org.objenesis:objenesis:jar:1.2"]
      end
    end
  end
  
  class CustomScalaTest < Buildr::Scala::ScalaTest
    VERSION = "1.4.1"
    class << self
      def dependencies
        ["org.scalatest:scalatest_#{Scala.version}:jar:#{version}"] + Check.dependencies + Mockito.dependencies
      end
    end
  end
  
end

Buildr::TestFramework << Buildr::Scala::CustomScalaTest

#Release.message = lambda { |version| "Tagged version #{version}" }
Release.next_version = lambda do |version| 
  v = version.gsub(/-SNAPSHOT$/, "").split(/\./)
  v[-1] = v[-1].to_i + 1
  v.join(".") + "-SNAPSHOT" 
end  
