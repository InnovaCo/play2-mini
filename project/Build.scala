import sbt._
import Keys._

object MinimalBuild extends Build {
  
  lazy val buildVersion =  "2.0-RC1"
  
  lazy val typesafePub = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/ivy-releases/"
  lazy val typesafeSnapshotPub = "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/ivy-snapshots/"
  lazy val typesafeSnapshot = "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
  lazy val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  lazy val repo = if (buildVersion.endsWith("SNAPSHOT")) typesafeSnapshot else typesafe  
  lazy val repoPublish = if (buildVersion.endsWith("SNAPSHOT")) typesafeSnapshotPub else typesafePub  
  

  lazy val play =  "play" %% "play" % buildVersion 

  lazy val root = Project(id = "play-mini", base = file("."), settings = Project.defaultSettings).settings(
    version := buildVersion,
    publishTo := Some(repoPublish),
    organization := "com.typesafe",
    resolvers += repo,
    libraryDependencies += play,
    mainClass in (Compile, run) := Some("play.core.server.NettyServer"),
    ivyXML :=
      <dependencies>
        <dependency org="maven-plugins" name="maven-plugins" rev="1.3.1">
          <exclude module="maven-cobertura-plugin"/>
          <exclude module="maven-findbugs-plugin"/>
        </dependency>
      </dependencies>
  )
}
