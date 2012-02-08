import sbt._
import Keys._

object MinimalBuild extends Build {
  
  lazy val root = Project(id = "play-mini", base = file("."), settings = Project.defaultSettings).settings(
    version := "2.0-RC1-SNAPSHOT",
    organization := "com.typesafe",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
    libraryDependencies += "play" %% "play" % "2.0-RC1-SNAPSHOT",
    mainClass in (Compile, run) := Some("play.core.server.NettyServer")
  )
  
 
