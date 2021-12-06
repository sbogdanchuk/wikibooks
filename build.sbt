name := "wikibooks"

version := "0.1"

Compile/mainClass := Some("org.wikibooks.meetups.meet02.LoadData")

addDependencyTreePlugin

scalaVersion := "2.12.10"
val AkkaVersion = "2.6.17"
val AkkaHttpVersion = "10.2.7"

//excludeDependencies += "commons-logging" % "commons-logging"
excludeDependencies ++= Seq(
  ExclusionRule(organization="org.apache.logging.log4j", name="log4j-slf4j-impl")
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "org.apache.spark" %% "spark-core" % "3.1.2" exclude("org.slf4j", "*"),
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
//  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.slf4j" % "slf4j-api" % "1.6.6",
  "org.slf4j" % "slf4j-log4j12" % "1.6.6",
  "org.xerial" % "sqlite-jdbc" % "3.7.2"
//  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
)
