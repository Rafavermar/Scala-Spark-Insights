import Dependencies.{catsDependencies, jmhDependencies, scalaFmtDependencies, sparkDependencies, testDependencies}

name := "Scala-Spark-Insights"
version := "1.0.0"
scalaVersion := Versions.Scala


libraryDependencies ++= sparkDependencies ++ catsDependencies ++ testDependencies ++
  jmhDependencies ++ scalaFmtDependencies

javaOptions ++= Seq(
  "-Xmx2G",
  "-Xms2G",
  "-XX:ReservedCodeCacheSize=512M",
  "-XX:+UseG1GC",
  "-XX:MaxGCPauseMillis=200",
  "-XX:G1ReservePercent=15",
  "-XX:InitiatingHeapOccupancyPercent=25",
  "-XX:+UseStringDeduplication"
)

Test / fork := true
Test / parallelExecution := false
enablePlugins(JmhPlugin)


// Application packing
enablePlugins(JavaAppPackaging)

dockerBaseImage := "openjdk:17-jre-slim"
Docker / version := "latest"
dockerExposedPorts := Seq(4040)
dockerUpdateLatest := true
dockerRepository := Some("spark.scala")
dockerEntrypoint := Seq("java", "-jar", "/opt/docker/lib/Scala-Spark-Insights.jar", "src/main/scala/week2/datasetsapi/DatasetsExApp03.scala") // Calling the main application class: src/main/scala/week2/datasetsapi/DatasetsExApp03.scala

dockerUpdateLatest := true
Docker / maintainer := "spark.scala"

// Copy the generated jar by sbt-assembly to Docker directory
Docker / stage := {
  // targetDir is the application output directory
  val targetDir = stage.value
  // assemblyOutputPath  is the jar name generated by sbt-assembly
  val jarFile = (assembly / assemblyOutputPath).value
  // Copy the jar file to Docker directory
  IO.copyFile(jarFile, targetDir / "lib" / jarFile.getName)
  targetDir
}


enablePlugins(AssemblyPlugin)
assembly / assemblyJarName := "Scala-Spark-Insights.jar"
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => (assembly / assemblyMergeStrategy).value(x)
}

// Add an Alias to the Dokcer Plugin
// First, package the application: assembly
addCommandAlias("publish-docker", ";assembly;docker:publishLocal")