package week2.bucketization

import org.apache.spark.sql.functions
import week2.sparksql.SparkSessionWrapper

/**
 * A Scala application demonstrating the use of data partitioning, bucketing, and grouping sets in Apache Spark.
 * This class shows how to optimize data layout and querying for large datasets using Spark SQL's capabilities.
 */
object GroupingSets02App extends App with SparkSessionWrapper {

  // Reduce log verbosity to focus on the application output
  spark.sparkContext.setLogLevel("ERROR")

  // Disable Adaptive Query Execution (AQE) for consistent performance measurements
  spark.conf.set("spark.sql.adaptive.enabled", "false")

  // Disable broadcast joins to force Spark to perform other join strategies
  spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "-1")

  // Set a reasonable number of shuffle partitions to manage parallelism
  spark.conf.set("spark.sql.shuffle.partitions", "10")

  import spark.implicits._

  // Original small dataset to demonstrate the concept
  val data = Seq(
    ("Banana", "Fruit", 1000, 1),
    ("Carrot", "Vegetable", 1000, 1),
    ("Bean", "Vegetable", 2000, 2),
    ("Orange", "Fruit", 2000, 2),
    ("Banana", "Fruit", 4000, 3),
    ("Carrot", "Vegetable", 4000, 3),
    ("Bean", "Vegetable", 3000, 0),
    ("Orange", "Fruit", 3000, 0)
  )

  // Scale up the dataset to a large size to simulate a real-world scenario
  val data2 = (0 until 1000000).flatMap { _ => data }

  // Variables for controlling the setup
  val usarExistentes = true
  val BucketSize = 4
  val BucketColumn = "Name"
  val TablaPorNombre = "sales_data_by_name"
  val TablaPorNombreYCategoria = "sales_data_by_name_and_category"

  // Either load existing tables or create new ones based on the `usarExistentes` flag
  if (!usarExistentes) {
    val df = data2.toDF(BucketColumn, "Category", "Sales", "Quantity")
    // Use bucketing to optimize query performance
    df.write.bucketBy(BucketSize, BucketColumn).saveAsTable(TablaPorNombre)
    df.write.partitionBy("Category").bucketBy(BucketSize, BucketColumn).saveAsTable(TablaPorNombreYCategoria)
  } else {
    println(s"Using existing tables $TablaPorNombre and $TablaPorNombreYCategoria")
    spark.read.load("./spark-warehouse/" + TablaPorNombre).createOrReplaceTempView(TablaPorNombre)
    spark.read.load("./spark-warehouse/" + TablaPorNombreYCategoria).createOrReplaceTempView(TablaPorNombreYCategoria)
  }

  // Load and query the bucketed data
  val dfConBucketing = spark.table(TablaPorNombre)
  println(s"Table $TablaPorNombre contains: ${dfConBucketing.count()} rows")

  // Execute SQL queries using grouping sets to summarize data
  val result = spark.sql(s"SELECT $BucketColumn, Category, sum(Sales) as TotalSales FROM $TablaPorNombre GROUP BY $BucketColumn, Category GROUPING SETS(($BucketColumn, Category))")

  // Rollup as an alternative method to grouping sets
  val resultDf = dfConBucketing.groupBy(BucketColumn, "Category").agg(functions.sum("Sales")).rollup(BucketColumn, "Category").count()

  // Explain the query execution plan
  result.explain(true)
  resultDf.explain(true)

  // Display results
  result.show()
  resultDf.show()

  // Repeat the process for the partitioned and bucketed data
  val dfConBucketingYParticionado = spark.table(TablaPorNombreYCategoria)
  val result2 = spark.sql(s"SELECT $BucketColumn, Category, sum(Sales) as TotalSales FROM $TablaPorNombreYCategoria GROUP BY $BucketColumn, Category GROUPING SETS(($BucketColumn, Category))")
  val resultDf2 = dfConBucketingYParticionado.groupBy(BucketColumn, "Category").agg(functions.sum("Sales")).rollup(BucketColumn, "Category").count()

  // Explain and show results
  result2.explain(true)
  resultDf2.explain(true)
  result2.show(truncate = false)
  resultDf2.show(truncate = false)
}
