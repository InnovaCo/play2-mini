import sbt._
import Keys._

object MinimalBuild extends Build {
  
  lazy val root = Project(id = "play-mini", base = file("."), settings = Project.defaultSettings).settings(
    version := "2.0-RC1-SNAPSHOT",
    organization := "com.typesafe",
    resolvers += Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
    libraryDependencies += "play" %% "play" % "2.0-RC1-SNAPSHOT",
    mainClass in (Compile, run) := Some("play.core.server.NettyServer")
  )
  
} 
