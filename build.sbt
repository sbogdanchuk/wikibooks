name := "wikibooks"

version := "0.1"

scalaVersion := "2.12.10"
val AkkaVersion = "2.6.17"

idePackagePrefix := Some("org.wikibooks")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
//  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "org.apache.spark" %% "spark-core" % "3.1.2"
)
