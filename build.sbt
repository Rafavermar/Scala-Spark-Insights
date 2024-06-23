name := "Scala-Spark-Insights"
version := "1.0.0"
scalaVersion := "2.13.14"

val VersionSpark = "3.5.1"
val VersionCatsCore = "2.12.0"
val VersionScalaTest = "3.2.18"
val VersionScalafmt = "3.8.1"

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % VersionSpark % Provided,
  "org.apache.spark" %% "spark-sql" % VersionSpark % Provided,
  "org.apache.spark" %% "spark-graphx" % VersionSpark % Provided
)

val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % VersionCatsCore
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % VersionScalaTest % Test,
  "com.github.mrpowers" %% "spark-fast-tests" % "1.3.0" % Test
)

val jmhDependencies = Seq(
  "org.openjdk.jmh" % "jmh-core" % "1.37",
  "org.openjdk.jmh" % "jmh-generator-annprocess" % "1.37"
)

val scalaFmtDependencies = Seq(
  "org.scalameta" %% "scalafmt-dynamic" % VersionScalafmt
)

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

// Add JMH alias with profiler options: -prof gc means garbage collection profiler
// -prof cl means class loading profiler
// -prof comp means compiler profiler
// -prof hs_gc means HotSpot garbage collection profiler
// -prof hs_cl means HotSpot class loading profiler
// -prof hs_comp means HotSpot compiler profiler
// Add JMH alias: -i 3 means 3 iterations, -wi 2 means 2 warm-up iterations, -f1 means 1 fork, -t1 means 1 thread

addCommandAlias("jmh", "Jmh/run -i 3 -wi 2 -f1 -t1")
addCommandAlias("prof", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof gc")
addCommandAlias("prof-cl", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof cl")
addCommandAlias("prof-comp", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof comp")
addCommandAlias("prof-gc", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof gc")
addCommandAlias("prof-all", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof gc -prof cl -prof comp")