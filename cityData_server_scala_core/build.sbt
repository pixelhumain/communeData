organization := "pixelhumain"

name := "semantic_rest_core"

version := "1.0-SNAPSHOT"

lazy val semantic_rest = (project in file("."))
	.enablePlugins(PlayScala)

scalaVersion := "2.11.6"


libraryDependencies += "org.w3" %%  "banana-rdf" % "0.8.1"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % Test

javacOptions ++= Seq("-source","1.7", "-target","1.7")

resolvers += Resolver.url("inthenow-releases", url("http://dl.bintray.com/inthenow/releases"))(Resolver.ivyStylePatterns)

// banana-rdf still has some dependencies that are not yet on Maven Central
resolvers += Resolver.file("Local repo", file(System.getProperty("user.home") + "/.ivy2/local"))(Resolver.ivyStylePatterns)


