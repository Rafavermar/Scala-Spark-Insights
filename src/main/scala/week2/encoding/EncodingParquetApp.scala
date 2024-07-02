package week2.encoding

import org.apache.spark.sql.SaveMode

/**
 * The `EncodingParquetApp` demonstrates using Apache Spark to handle data in Parquet format.
 * Parquet is a columnar storage file format optimized for use with big data processing frameworks.
 *
 * This object showcases:
 * - Writing and reading Parquet files.
 * - Schema evolution with Parquet to manage changes in data structure over time.
 * - Demonstrating the merge of schemas to accommodate data recorded with different schema versions.
 *
 * Scaladoc tags:
 * @example To execute this application, ensure that the Spark session is configured for local execution.
 *          It is suitable for demonstrations in development environments where configuration and resource management can be handled on a single machine.
 */
object EncodingParquetApp extends App {

  // Initialize SparkSession
  import org.apache.spark.sql.SparkSession
  val spark = SparkSession.builder()
    .appName("EncodingParquetApp")
    .master("local[*]")
    .getOrCreate()

  // Set log level to minimize console output except for errors
  spark.sparkContext.setLogLevel("ERROR")

  // Import implicits to use standard encoders and toDF method
  import spark.implicits._

  // Define initial dataset and write to Parquet file
  val data = Seq(
    (1, "Alice", 28, "v1"),
    (2, "Bob", 25, "v1"),
    (3, "Charlie", 30, "v1")
  )

  val df = data.toDF("id", "name", "age", "version")
  df.write
    .format("parquet")
    .mode(SaveMode.Overwrite)
    .save("src/main/scala/week2/encoding/data/parquet/person_v1")

  // Read the Parquet file back into a DataFrame and display contents
  val dfRead = spark.read
    .format("parquet")
    .load("src/main/scala/week2/encoding/data/parquet/person_v1")
  dfRead.show()

  // Output the schema of the DataFrame read from the Parquet file
  val schemaRead = dfRead.schema
  println(schemaRead)

  // Define second version of dataset with an additional column 'city'
  val dataV2 = Seq(
    (1, "Alice", 28, "v2", "Lion"),
    (2, "Bob", 25, "v2", "Valencia"),
    (3, "Charlie", 30, "v2", "Madrid")
  )

  val dfV2 = dataV2.toDF("id", "name", "age", "version", "city")
  dfV2.write
    .format("parquet")
    .mode(SaveMode.Overwrite)
    .save("src/main/scala/week2/encoding/data/parquet/person_v2")

  val dfReadV2 = spark.read
    .format("parquet")
    .load("src/main/scala/week2/encoding/data/parquet/person_v2")
  dfReadV2.show()

  val schemaReadV2 = dfReadV2.schema
  println(schemaReadV2)

  // Demonstrate reading with an evolving schema: add 'country' in version 3
  val dataV3 = Seq(
    (1, "Alice", 28, "v3", "Lion", "France"),
    (2, "Bob", 25, "v3", "Valencia", "Spain"),
    (3, "Charlie", 30, "v3", "Madrid", "Spain")
  )

  val dfV3 = dataV3.toDF("id", "name", "age", "version", "city", "country")
  dfV3.write
    .format("parquet")
    .mode(SaveMode.Overwrite)
    .save("src/main/scala/week2/encoding/data/parquet/person_v3")

  val dfReadV3 = spark.read
    .format("parquet")
    .load("src/main/scala/week2/encoding/data/parquet/person_v3")
  dfReadV3.show()

  val schemaReadV3 = dfReadV3.schema
  println(schemaReadV3)

  // Read all versions with merged schemas to handle schema evolution
  val dfReadV1_2_3 = spark.read
    .option("mergeSchema", "true")
    .schema(schemaReadV3)
    .format("parquet")
    .load("src/main/scala/week2/encoding/data/parquet/person_v2", "src/main/scala/week2/encoding/data/parquet/person_v3", "src/main/scala/week2/encoding/data/parquet/person_v1")

  dfReadV1_2_3.show()
}

