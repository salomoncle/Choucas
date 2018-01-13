name := "Choucas"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.11"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.7" // or whatever the latest version is
libraryDependencies += "com.typesafe.akka" %% "akka-actor"  % "2.5.6"
libraryDependencies += "io.swagger" %% "swagger-scala-module" % "1.0.3"
libraryDependencies += "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0"
libraryDependencies += "org.scalaj" % "scalaj-http_2.12" % "2.3.0"