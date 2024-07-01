package week2.encoding

/**
 * The `EncodingAvroApp` object demonstrates the use of Apache Avro for data serialization
 * in a Spark environment. Avro is a binary serialization format that integrates seamlessly
 * with Apache Spark, providing robust support for schema evolution and interoperability across
 * different programming languages. This class covers the fundamentals of Avro serialization,
 * including reading and writing Avro files with evolving schemas.
 *
 * Functionalities:
 * - **Schema Evolution**: Demonstrates how Avro supports schema evolution, allowing serialized data
 *   to be read by systems with different versions of the schema. This is crucial for long-term data
 *   storage and processing systems where schemas may need to change over time without breaking existing
 *   applications.
 *
 * - **Data Serialization and Deserialization**: Showcases how to serialize data into Avro format and
 *   deserialize it back into Spark DataFrames, enabling efficient data storage and interchange.
 *
 * - **Version Handling**: Uses versioning in data schemas to manage changes and ensure compatibility
 *   across different data versions.
 *
 * - **Schema Management**: Reads Avro schema definitions from external files, demonstrating how to
 *   manage and utilize complex schemas outside of application code.
 *
 * Example Usage:
 * - Ideal for testing and demonstrating data serialization strategies in enterprise data lakes,
 *   ETL pipelines, and data warehousing solutions.
 */
import org.apache.avro.Schema
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import scala.io.Source
import scala.util.{Try, Using}

object SchemaUtils {
  // Utility to read and parse an Avro schema file
  def parseSchema(schemaFile: String): Try[Schema] = {
    Using(Source.fromFile(schemaFile)) { source =>
      val schemaString = source.mkString
      new Schema.Parser().parse(schemaString)
    }
  }
}

object DataFrameExtensions {
  // Extension method to write a DataFrame in Avro format
  implicit class AvroDataFrameWriter(df: DataFrame) {
    def writeAvro(filePath: String, schema: Schema): Unit = {
      df.write
        .format("avro")
        .mode(SaveMode.Overwrite)
        .option("avroSchema", schema.toString)
        .save(filePath)
    }
  }
}

object EncodingAvroApp extends App {
  val spark = SparkSession.builder()
    .appName("EncodingAvroApp")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")
  import DataFrameExtensions._
  import spark.implicits._

  // Version 1 Data and Schema
  val dataV1 = Seq((1, "Alice", 28, "v1"), (2, "Bob", 25, "v1"), (3, "Charlie", 30, "v1"))
  val dfV1 = dataV1.toDF("id", "name", "age", "version")

  val schemaV1Path = "src/main/resources/avro/person_v1.avsc"
  val avroSchemaV1 = SchemaUtils.parseSchema(schemaV1Path).getOrElse(throw new IllegalArgumentException("Invalid Schema"))

  // Write and read Avro data with schema V1
  dfV1.writeAvro("src/main/scala/week2/encoding/data/avro/person_v1", avroSchemaV1)
  val dfReadV1 = spark.read.format("avro").load("src/main/scala/week2/encoding/data/avro/person_v1")
  dfReadV1.show()

  // Version 2 Data and Schema
  val dataV2 = Seq((1, "Alice", 28, "v2", "Lyon"), (2, "Bob", 25, "v2", "Valencia"), (3, "Charlie", 30, "v2", "Madrid"))
  val dfV2 = dataV2.toDF("id", "name", "age", "version", "city")

  val schemaV2Path = "src/main/resources/avro/person_v2.avsc"
  val avroSchemaV2 = SchemaUtils.parseSchema(schemaV2Path).getOrElse(throw new IllegalArgumentException("Invalid Schema"))

  // Write and read Avro data with schema V2
  dfV2.writeAvro("src/main/scala/week2/encoding/data/avro/person_v2", avroSchemaV2)
  val dfReadV2 = spark.read.format("avro").load("src/main/scala/week2/encoding/data/avro/person_v2")
  dfReadV2.show()

  spark.stop()
}