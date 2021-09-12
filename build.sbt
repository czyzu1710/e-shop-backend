name := """e-shop-backend"""
organization := "pl.edu.uj"
maintainer := "michal.czyz@student.uj.edu.pl"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
libraryDependencies += "com.h2database" % "h2" % "1.4.200"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.36.0.3"

libraryDependencies += "net.coobird" % "thumbnailator" % "0.4.14"



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "pl.edu.uj.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "pl.edu.uj.binders._"
