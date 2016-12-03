name := "sallo"

version := "1.0"

scalaVersion := "2.12.0"


libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "commons-codec" % "commons-codec" % "1.9"
)

//assemblyJarName in assembly := "scala-library-2.12.0.jar"