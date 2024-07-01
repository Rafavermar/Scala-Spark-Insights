package week2.sparkapp

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * Configuration for Spark applications with a focus on both local and testing setups.
 */
object FirstSparkAppConfig {

  /**
   * Configuration for local development. Sets the application name, master node, various memory and core settings
   * optimized for a local machine running multiple threads.
   */
  val sparkConfLocal: SparkConf = new SparkConf()
    .setAppName("PrimeraAppSparkConf")
    .setMaster("local[*]")
    .set("spark.shuffle.partitions", "5")
    .set("spark.sql.shuffle.partitions", "5")
    .set("spark.driver.memory", "2g")
    .set("spark.driver.cores", "1")
    .set("spark.executor.memory", "2g")
    .set("spark.executor.cores", "2")
    .set("spark.executor.instances", "2")

  /**
   * Configuration for testing environments. Tailored to manage lower resources and disable UI to speed up tests.
   */
  val sparkConfTesting: SparkConf = new SparkConf()
    .setAppName("PrimeraAppSparkConf")
    .setMaster("local[2]")
    .set("spark.shuffle.partitions", "5")
    .set("spark.sql.shuffle.partitions", "5")
    .set("spark.driver.memory", "512m")
    .set("spark.driver.cores", "1")
    .set("spark.executor.memory", "512m")
    .set("spark.executor.cores", "1")
    .set("spark.ui.enabled", "false")
    .set("spark.sql.autoBroadcastJoinThreshold", "-1")
    .set("spark.dirs", "/tmp/spark-temp")
}

/**
 * Generator for synthetic data to be used in Spark applications.
 */
object PrimeraAppSparkDataGen {
  private val maxAge = 75

  /**
   * Generates a sequence of tuples with names and ages.
   *
   * @param numRecords The number of records to generate.
   * @return A sequence of (String, Int) tuples representing names and ages.
   */
  def generateData(numRecords: Int): Seq[(String, Int)] = {
    val data = (1 to numRecords).map { i =>
      val name = s"Name$i"
      val age = scala.util.Random.nextInt(maxAge)
      (name, age)
    }
    data
  }
}

/**
 * Main application for running Spark data operations.
 */
object FirstSparkApp extends App {

  /**
   * Entry point for the Spark application. Sets up the Spark session, generates data, processes it,
   * and outputs results to a CSV file.
   */
  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("PrimeraAppSpark")
    .getOrCreate()

  import spark.implicits._

  val data: Seq[(String, Int)] = PrimeraAppSparkDataGen.generateData(100000)
  val df: DataFrame = data.toDF("name", "age")

  df.show(truncate = false)

  val mayoresDeVeintidos: DataFrame = df.filter($"age" > 22)
  mayoresDeVeintidos.show(truncate = false)
  mayoresDeVeintidos.write.mode(SaveMode.Overwrite)
    .csv("out/mayoresDeVeintidos.csv")

  spark.stop()
}
