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


// Empaquetar la aplicación
enablePlugins(JavaAppPackaging)

dockerBaseImage := "openjdk:11-jre-slim"
Docker / version := "latest"
dockerExposedPorts := Seq(4040)
dockerUpdateLatest := true
dockerRepository := Some("eoi.de")
dockerEntrypoint := Seq("java", "-jar", "/opt/docker/lib/spark-eoi.jar", "eoi.de.examples.spark.sql.datasets.EjemploDatasetsApp03") // Llamamos a la clase principal de la aplicación: eoi.de.examples.spark.sql.datasets.EjemploDatasetsApp03

dockerUpdateLatest := true
Docker / maintainer := "eoi.de"

// Copiamos el jar generado por sbt-assembly al directorio de Docker
Docker / stage := {
  // targetDir es el directorio de salida de la aplicación
  val targetDir = stage.value
  // assemblyOutputPath es el nombre del jar generado por sbt-assembly
  val jarFile = (assembly / assemblyOutputPath).value
  // Copiamos el jar al directorio de Docker
  IO.copyFile(jarFile, targetDir / "lib" / jarFile.getName)
  targetDir
}


enablePlugins(AssemblyPlugin)
assembly / assemblyJarName := "spark-eoi.jar"
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => (assembly / assemblyMergeStrategy).value(x)
}



// Añadimos command alias para el plugin de Docker
// Primero hay que empaquetar la aplicación: assembly
addCommandAlias("publicar-docker", ";assembly;docker:publishLocal")