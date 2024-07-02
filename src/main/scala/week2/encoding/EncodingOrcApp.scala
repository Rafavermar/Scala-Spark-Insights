package week2.encoding

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * The `EncodingOrcApp` object demonstrates how to read from and write to ORC files using Apache Spark.
 * ORC (Optimized Row Columnar) format is a type of columnar storage that provides efficient ways
 * of storing data in a highly optimized and compressed format, suitable for big data processing.
 *
 * Key features:
 * - Writing and reading data in ORC format.
 * - Compression options to enhance storage efficiency.
 * - Schema merging to handle evolving data structures over multiple datasets.
 * - Handling of missing data by filling in default values for specified columns.
 */
object EncodingOrcApp extends App {

  // Initialize SparkSession with a local master
  val spark = SparkSession.builder()
    .appName("EncodingOrcApp")
    .master("local[*]")
    .getOrCreate()

  // Set log level to ERROR to reduce console output clutter
  spark.sparkContext.setLogLevel("ERROR")

  // Import implicits for automatic conversions like converting RDDs to DataFrames
  import spark.implicits._

  // Example data set 1
  val dataV1 = Seq(
    (1, "Alice", 28, "v1"),
    (2, "Bob", 25, "v1"),
    (3, "Charlie", 30, "v1")
  )

  // Convert to DataFrame and write to ORC file with zlib compression
  val dfV1 = dataV1.toDF("id", "name", "age", "version")
  dfV1.write
    .format("orc")
    .mode(SaveMode.Overwrite)
    .option("compression", "zlib")
    .save("src/main/scala/week2/encoding/data/orc/person_v1")

  // Read the ORC file back into a DataFrame and show the results
  val dfReadV1 = spark.read
    .format("orc")
    .load("src/main/scala/week2/encoding/data//orc/person_v1")
  dfReadV1.show()

  // Print schema of the DataFrame read from ORC
  val schemaReadV1 = dfReadV1.schema
  println(schemaReadV1)

  // Example data set 2 with an additional column 'city'
  val dataV2 = Seq(
    (1, "Alice", 28, "v2", "Lion"),
    (2, "Bob", 25, "v2", "Valencia"),
    (3, "Charlie", 30, "v2", "Madrid")
  )

  val dfV2 = dataV2.toDF("id", "name", "age", "version", "city")
  dfV2.write
    .format("orc")
    .mode(SaveMode.Overwrite)
    .option("compression", "zlib")
    .save("src/main/scala/week2/encoding/data//orc/person_v2")

  val dfReadV2 = spark.read
    .format("orc")
    .load("src/main/scala/week2/encoding/data/orc/person_v2")
  dfReadV2.show()

  val schemaReadV2 = dfReadV2.schema
  println(schemaReadV2)

  // Read both versions together, demonstrating schema merging
  val dfReadV1_2 = spark.read
    .format("orc")
    .load("src/main/scala/week2/encoding/data/orc/person_v1", "src/main/scala/week2/encoding/data/orc/person_v2")
  dfReadV1_2.show()

  // Example data set 3 with an additional column 'country'
  val dataV3 = Seq(
    (1, "Alice", 28, "v3", "Lion", "Spain"),
    (2, "Bob", 25, "v3", "Valencia", "Spain"),
    (3, "Charlie", 30, "v3", "Madrid", "Spain")
  )

  val dfV3 = dataV3.toDF("id", "name", "age", "version", "city", "country")
  dfV3.write
    .format("orc")
    .mode(SaveMode.Overwrite)
    .option("compression", "zlib")
    .save("src/main/scala/week2/encoding/data/orc/person_v3")

  val dfReadV3 = spark.read
    .format("orc")
    .load("src/main/scala/week2/encoding/data/orc/person_v3")
  dfReadV3.show()

  val schemaReadV3 = dfReadV3.schema
  println(schemaReadV3)

  // Read all versions with schema merging
  val dfReadV1_2_3 = spark.read
    .option("mergeSchema", "true")
    .format("orc")
    .load("src/main/scala/week2/encoding/data/orc/person_v1", "src/main/scala/week2/encoding/data/orc/person_v2", "src/main/scala/week2/encoding/data/orc/person_v3")
  dfReadV1_2_3.show()

  // Fill 'null' values for 'city' and 'country' with "Unknown"
  val dfReadV1_2_3Filled = dfReadV1_2_3
    .na.fill("Unknown", Seq("city", "country"))
  dfReadV1_2_3Filled.show(false)

  // Save the filled DataFrame back to ORC
  dfReadV1_2_3Filled
    .write
    .format("orc")
    .mode(SaveMode.Overwrite)
    .option("compression", "zlib")
    .save("src/main/scala/week2/encoding/data/orc/person_v1_2_3")

}
