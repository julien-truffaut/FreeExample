
import sbt._

name := "FreeExample"

version := "0.1"

scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "org.typelevel"   %% "cats"        % "0.4.1",
  "org.scalaz"      %% "scalaz-concurrent" % "7.2.0",
  "org.scalatest"   %% "scalatest"   % "2.2.4"  % "test"
)

