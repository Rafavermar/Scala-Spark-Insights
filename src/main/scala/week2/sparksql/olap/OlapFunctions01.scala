package week2.sparksql.olap

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

/**
 * Use of various window functions in Spark SQL for OLAP purposes.
 * This application shows how to compute different statistical measures across windows defined in a DataFrame.
 */
object OlapFunctions01 extends App {

  // Initialize SparkSession with configuration suitable for local testing.
  val spark = SparkSession.builder
    .appName("Spark SQL OLAP functions")
    .config("spark.master", "local")
    .getOrCreate()

  // Set the log level to ERROR to reduce the amount of log output.
  spark.sparkContext.setLogLevel("ERROR")

  // Import implicits to enable the $"column_name" syntax and other DataFrame transformations.
  import spark.implicits._

  // Sample data simulating a typical use case in OLAP tasks.
  val data = Seq(
    ("A", 19), ("A", 2), ("A", 19), ("A", 10), ("A", 20),
    ("B", 6), ("B", 5), ("B", 10),
    ("C", 7), ("C", 8), ("C", 1)
  )

  // Create DataFrame from the sample data.
  val df = data.toDF("key", "value")

  // Define a window specification which partitions data by 'key' and orders by 'value'.
  val windowSpec = Window.partitionBy("key").orderBy("value")

  // Overview of the window functions demonstrated.
  println("WindowSpec: lag, lead, rank, dense_rank, percent_rank, ntile, cume_dist, row_number ")

  // LAG function to access previous row's value.
  println("lag: returns the value of the column in the previous row")
  val df2 = df.withColumn("lag", lag("value", 1).over(windowSpec))
  df2.show()

  // LEAD function to access next row's value.
  println("lead: returns the value of the column in the next row")
  val df3 = df.withColumn("lead", lead("value", 1).over(windowSpec))
  df3.show()

  // RANK function to provide a rank to each row within a partition.
  println("rank: returns the rank of the current row")
  val df4 = df.withColumn("rank", rank().over(windowSpec))
  df4.show()

  // DENSE_RANK function to provide a rank to each row within a partition without gaps.
  println("dense_rank: returns the dense rank of the current row")
  val df5 = df.withColumn("dense_rank", dense_rank().over(windowSpec))
  df5.show()

  // PERCENT_RANK function to provide a relative rank of each row within a partition.
  println("percent_rank: returns the percentage rank of the current row")
  val df6 = df.withColumn("percent_rank", percent_rank().over(windowSpec))
  df6.show()

  // NTILE function to divide rows into a specified number of roughly equal groups within a partition.
  println("ntile: returns the ntile group of the current row")
  val df7 = df.withColumn("ntile", ntile(2).over(windowSpec))
  df7.show()

  // CUME_DIST function to provide the cumulative distribution of values within a partition.
  println("cume_dist: returns the cumulative distribution of the current row")
  val df8 = df.withColumn("cume_dist", cume_dist().over(windowSpec))
  df8.show()

  // ROW_NUMBER function to provide a unique sequential number to each row, starting from 1, within a partition.
  println("row_number: returns the row number of the current row")
  val df9 = df.withColumn("row_number", row_number().over(windowSpec))
  df9.show()

  // Stop the Spark session
  spark.stop()
}
