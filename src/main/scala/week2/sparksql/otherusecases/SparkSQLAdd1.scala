package week2.sparksql.otherusecases

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.catalog.Catalog

import scala.Console.{BOLD, RESET}

/**
 * Basic usage of Spark SQL for data processing and manipulation.
 *
 * This object showcases how to set up a Spark session, configure its properties, and use it to read,
 * transform, and manage datasets. It also illustrates how to interact with Spark's catalog to handle databases
 * and tables dynamically.
 */
object SparkSQLAdd1 extends App {

  /**
   * Sets up the Spark session with application name and master configuration.
   * SparkSession is the entry point to programming Spark with the Dataset and DataFrame API.
   */
  implicit val spark: SparkSession = SparkSession.builder()
    .appName("SparkSQLIntro01") // App name as it appears in the Spark UI
    .master("local[*]") // Run locally using all available cores
    .getOrCreate()

  // Set the log level to "WARN" to reduce the amount of console output from Spark
  spark.sparkContext.setLogLevel("WARN")

  // Print the Spark version to the console
  println(s"Spark version: ${spark.version}")

  // Print Spark configuration settings
  println(s"Spark conf: ${spark.conf}")
  spark.conf.getAll.foreach(println)
  println(s"Spark conf spark.app.name: ${spark.conf.get("spark.app.name")}")

  // Aligning configuration output for better readability
  val maxKeyLength = spark.conf.getAll.map(_._1.length).max + 1
  spark.conf.getAll.foreach {
    case (k, v) =>
      println(s"${k.padTo(maxKeyLength, ' ')} : $v")
  }
  println()

  // Print Spark context details
  println(s"Spark context: ${spark.sparkContext}")
  println()

  // Show the list of databases in Spark's catalog
  spark.catalog.listDatabases().show(truncate = false)

  // Load a DataFrame from a JSON file
  val df: DataFrame = spark.read
    .option("inferSchema", "true")
    .json("src/main/resources/sample_data/employees.json")

  df.printSchema()
  df.show(truncate = false)
  df.select("salary").show(truncate = false)

  // Create or replace a temporary view of the DataFrame
  df.createOrReplaceTempView("employees")
  spark.catalog.listTables().show(truncate = false)

  // Handling databases using SQL queries and Spark's catalog
  if (!spark.catalog.databaseExists("ejemplo1")) {
    spark.sql("CREATE DATABASE ejemplo1")
  }
  spark.catalog.setCurrentDatabase("ejemplo1")
  println(s"La BD actual es: ${spark.catalog.currentDatabase}")
  println()

  // Display the tables in the current database
  println(BOLD + "Mostrar las tablas de la BD actual:" + RESET)
  val catalog: Catalog = spark.catalog
  catalog.listTables().collect().foreach { table =>
    println(table)
    println(s" -- Tabla: ${table.name}")
    catalog.listColumns(table.name).collect().foreach(column =>
      println("    - " + column)
    )
  }

  // Drop the created database and stop the Spark session
  spark.sql("DROP DATABASE ejemplo1 CASCADE")
  spark.stop()
  System.exit(0) // Exit the application cleanly
}
