name := "WhatsApp"

version := "0.1"

fork in run := true

scalaVersion := "2.12.4"

libraryDependencies := Seq(
//    "com.typesafe.akka" %% "akka-actor" % "2.4.20",
//    "com.typesafe.akka" %% "akka-persistence" % "2.4.20",
//    "com.typesafe.akka" %% "akka-remote" % "2.4.20",
//    "com.typesafe.akka" %% "akka-cluster" % "2.4.20",
//    "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.20",
//    "org.iq80.leveldb" % "leveldb" % "0.7",
//    "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
//    "com.typesafe.akka" %% "akka-http-core" % "10.0.11",
//    "com.typesafe.akka" %% "akka-http" % "10.0.11",
//    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11"

  "com.typesafe.akka" %% "akka-actor" % "2.5.7",
  "com.typesafe.akka" %% "akka-persistence" % "2.5.7",
  "com.typesafe.akka" %% "akka-remote" % "2.5.7",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.7",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.5.7",
  "com.typesafe.akka" %% "akka-http-core" % "10.0.11",
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
  "org.fusesource.leveldbjni"  % "leveldbjni-all" % "1.8",
  "org.iq80.leveldb" % "leveldb" % "0.7",
)