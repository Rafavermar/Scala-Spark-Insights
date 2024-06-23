import sbt.Provided
import sbt.*

object Dependencies {

  val sparkDependencies = Seq(
    "org.apache.spark" %% "spark-core" % Versions.Spark % Provided,
    "org.apache.spark" %% "spark-sql" % Versions.Spark % Provided,
    "org.apache.spark" %% "spark-graphx" % Versions.Spark % Provided
  )

  val catsDependencies = Seq(
    "org.typelevel" %% "cats-core" % Versions.CatsCore
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % Versions.ScalaTest % Test,
    "com.github.mrpowers" %% "spark-fast-tests" % "1.3.0" % Test
  )

  val jmhDependencies = Seq(
    "org.openjdk.jmh" % "jmh-core" % Versions.Jmh,
    "org.openjdk.jmh" % "jmh-generator-annprocess" % Versions.Jmh,
  )

  val scalaFmtDependencies = Seq(
    "org.scalameta" %% "scalafmt-dynamic" % Versions.Scalafmt
  )

}
