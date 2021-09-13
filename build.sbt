name := """e-shop-backend"""
organization := "pl.edu.uj"
maintainer := "michal.czyz@student.uj.edu.pl"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
libraryDependencies += "com.h2database" % "h2" % "1.4.200"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.36.0.3"

libraryDependencies += "net.coobird" % "thumbnailator" % "0.4.14"

libraryDependencies ++= Seq(
  "com.iheart" %% "ficus" % "1.5.1",
  "com.mohiva" %% "play-silhouette" % "7.0.0",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "7.0.0",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "7.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "7.0.0",
  "com.mohiva" %% "play-silhouette-testkit" % "7.0.0" % "test",
  "net.codingwell" %% "scala-guice" % "5.0.1"
)
libraryDependencies += ehcache

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers += "Atlassian's Maven Public Repository" at "https://packages.atlassian.com/maven-public/"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "pl.edu.uj.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "pl.edu.uj.binders._"
