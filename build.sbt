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