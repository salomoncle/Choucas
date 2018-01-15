name := "Choucas"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.11"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.7" // or whatever the latest version is
libraryDependencies += "com.typesafe.akka" %% "akka-actor"  % "2.5.6"
libraryDependencies += "io.swagger" %% "swagger-scala-module" % "1.0.3"
libraryDependencies += "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0"
libraryDependencies += "org.scalaj" % "scalaj-http_2.12" % "2.3.0"
libraryDependencies += "ch.qos.logback"          %  "logback-classic" % "1.2.3"
libraryDependencies += "net.databinder.dispatch" %% "dispatch-core"   % "0.13.3"
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.11"
libraryDependencies += "org.json4s" %% "json4s-ext" % "3.2.11"
libraryDependencies += "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.13.3"

val elastic4sVersion = "5.5.3"
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  // for the tcp client
  "com.sksamuel.elastic4s" %% "elastic4s-tcp" % elastic4sVersion,

  // for the http client
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,

  // if you want to use reactive streams
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % elastic4sVersion,

  // a json library
  "com.sksamuel.elastic4s" %% "elastic4s-jackson" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-play-json" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-circe" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-json4s" % elastic4sVersion,

  // testing
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test",
  "com.sksamuel.elastic4s" %% "elastic4s-embedded" % elastic4sVersion % "test"
)

//libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5")
