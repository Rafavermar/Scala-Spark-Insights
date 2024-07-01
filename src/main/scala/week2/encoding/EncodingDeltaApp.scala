package week2.encoding

/**
 * The `EncodingDeltaApp` object demonstrates the integration of Delta Lake with Apache Spark
 * for robust data management and schema evolution. Delta Lake provides ACID transactions,
 * scalable metadata handling, and unifies streaming and batch data processing.
 *
 * Functionalities:
 * - **Delta Lake Configuration**: Configures Spark to leverage Delta Lake by setting necessary
 *   configurations for SparkSession. This includes enabling Delta Lake's specific Spark SQL
 *   extensions and catalog.
 *
 * - **Data Versioning and Schema Evolution**: Utilizes Delta Lake to handle multiple versions
 *   of datasets, showcasing how to evolve data schema over time without disrupting downstream
 *   systems. It covers writing and reading data with different schema versions and merging them.
 *
 * - **Data Retention Management**: Demonstrates configuration settings to manage data retention
 *   policies in Delta Lake, crucial for handling large datasets and compliance requirements.
 *
 * Use Cases:
 * - Ideal for scenarios where data schemas need to evolve over time, such as in data lakes that
 *   accumulate data from various sources with changing data structures.
 * - Useful in environments requiring robust data integrity and consistency, provided by ACID
 *   transactions in Delta Lake.
 *
 * Example Usage:
 * - Can be employed in enterprise data platforms for incremental data ingestion, historical data
 *   management, and retrospective analysis with evolving schemas.
 */
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object EncodingDeltaApp extends App {

  val spark: SparkSession = initializeSparkSession()
  import spark.implicits._

  processData()

  private def initializeSparkSession(): SparkSession = {
    SparkSession.builder()
      .appName("EncodingDeltaApp")
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
      .config("spark.databricks.delta.retentionDurationCheck.enabled", "false")
      .master("local[*]")
      .getOrCreate()
  }

  private def processData(): Unit = {
    val paths = Map(
      "v1" -> "src/main/scala/week2/encoding/data/delta/person_v1",
      "v2" -> "src/main/scala/week2/encoding/data/delta/person_v2",
      "v3" -> "src/main/scala/week2/encoding/data/delta/person_v3"
    )

    val dataV1 = Seq((1, "Alice", 28, "v1"), (2, "Bob", 25, "v1"), (3, "Charlie", 30, "v1"))
    val dfV1 = writeAndRead(dataV1.toDF("id", "name", "age", "version"), paths("v1"))

    val dataV2 = Seq((1, "Alice", 28, "v2", "Lion"), (2, "Bob", 25, "v2", "Valencia"), (3, "Charlie", 30, "v2", "Madrid"))
    val dfV2 = writeAndRead(dataV2.toDF("id", "name", "age", "version", "city"), paths("v2"))

    val dfUnionV1V2 = dfV1.withColumn("city", lit("Unknown")).union(dfV2)
    dfUnionV1V2.show()

    val dataV3 = Seq((1, "Alice", 28, "v3", "Lion", "Spain"), (2, "Bob", 25, "v3", "Valencia", "Spain"), (3, "Charlie", 30, "v3", "Madrid", "Spain"))
    val dfV3 = writeAndRead(dataV3.toDF("id", "name", "age", "version", "city", "country"), paths("v3"))

    val finalUnion = dfUnionV1V2.withColumn("country", lit("Unknown")).union(dfV3)
    finalUnion.show()
  }

  private def writeAndRead(df: DataFrame, path: String): DataFrame = {
    df.write.format("delta").mode(SaveMode.Overwrite).save(path)
    spark.read.format("delta").option("mergeSchema", "true").load(path)
  }
}
