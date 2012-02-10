import sbt._
import Keys._

object MinimalBuild extends Build {
  
  lazy val buildVersion =  "2.0-RC1-SNAPSHOT"
  lazy val typesafeVersion = if (buildVersion.endsWith("SNAPSHOT")) "snapshots" else "releases"
  
  lazy val root = Project(id = "play-mini", base = file("."), settings = Project.defaultSettings).settings(
    version := buildVersion,
    organization := "com.typesafe",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/"+typesafeVersion,
    libraryDependencies += "play" %% "play" % buildVersion,
    mainClass in (Compile, run) := Some("play.core.server.NettyServer")
  )
}
