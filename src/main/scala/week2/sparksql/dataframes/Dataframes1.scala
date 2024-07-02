package week2.sparksql.dataframes

import org.apache.spark.sql.{Column, SaveMode}

/**
 * The `Dataframes1` object provides a concise demonstration of various operations using
 * Apache Spark's DataFrame API. It showcases the creation, manipulation, and storage of
 * DataFrames, emphasizing Spark's capabilities in handling data transformation and storage efficiently.
 *
 * This example includes:
 * - Creating DataFrames from sequences.
 * - Displaying DataFrame content and schema.
 * - Filtering data based on specific conditions.
 * - Enhancing DataFrames by adding new calculated columns.
 * - Saving DataFrames in different file formats like CSV, Parquet, and JSON.
 * - Reading data back from these formats to validate data integrity and format-specific features.
 */
object Dataframes1 extends App {
  import org.apache.spark.sql.SparkSession
  import org.apache.spark.sql.functions._

  // Initialize SparkSession
  val spark = SparkSession.builder()
    .appName("Dataframes1")
    .master("local[2]") // Use two local worker threads
    .getOrCreate()

  // Reduce the amount of console output from Spark
  spark.sparkContext.setLogLevel("ERROR")

  // Create a DataFrame from a sequence of tuples
  val data = Seq(("Alice", 34), ("Bob", 45), ("Charlie", 23))
  val df = spark.createDataFrame(data).toDF("name", "age")

  // Show DataFrame contents in various ways
  df.show()
  df.show(truncate = false)
  df.show(numRows = 2, truncate = false)

  // Print the DataFrame's schema
  df.printSchema()

  // Filter DataFrame to find rows where age is greater than 22
  val mayoresDeVeintidos = df.filter(col("age") > 22)
  mayoresDeVeintidos.show(false)
  val criterioEdad: Column = col("age") > 22
  val mayoresDeVeintidos2 = df.filter(criterioEdad)
  mayoresDeVeintidos2.show(false)

  // Add a new column with incremented age
  val columnaAdicional = (col("age") + 1).alias("age2")
  val df2 = df.withColumn("age2", columnaAdicional)
  df2.show(false)

  // Define a user-defined function (UDF) to double the age and add it as a new column
  val doblarEdad = udf((age: Int) => age * 2)
  val columnaDobleEdad = doblarEdad(col("age")).alias("ageDoble")
  val df3 = df.withColumn("ageDoble", columnaDobleEdad)
  df3.show(false)

  // Double age without using a UDF
  val columnaDobleEdad2 = col("age") * 2
  val df4 = df.withColumn("ageDoble", columnaDobleEdad2)
  df4.show(false)

  // Save DataFrame in different formats
  df.write.mode(SaveMode.Overwrite).csv("src/main/scala/week2/sparksql/out/DataFrames1.csv")
  df.write.mode(SaveMode.Append).parquet("src/main/scala/week2/sparksql/out/DataFrames1.parquet")
  df.write.mode(SaveMode.ErrorIfExists).json("src/main/scala/week2/sparksql/out/DataFrames1.json")

  // Demonstrate handling of files and data formats
  val csvDisco = spark.read
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .csv("src/main/scala/week2/sparksql/out/DataFrames1.csv")
  csvDisco.show(false)

  val parquetDisco = spark.read.parquet("src/main/scala/week2/sparksql/out/DataFrames1.parquet")
  parquetDisco.show(false)

  val jsonDisco = spark.read
    .option("multiLine", "true")
    .option("mode", "PERMISSIVE")
    .option("inferSchema", "true")
    .json("src/main/scala/week2/sparksql/out/DataFrames1.json")
  jsonDisco.show(false)
}

