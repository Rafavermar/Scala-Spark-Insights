package week2.rdd

import org.apache.spark.sql.functions._
import week2.rdd.SparkSetup.spark

/**
 * The `DataFrameOperations` object illustrates the use of Spark DataFrames for processing and analyzing
 * textual data. This class focuses on converting RDDs to DataFrames, applying transformations, and
 * performing aggregations, showcasing the power and ease of use of Spark SQL's DataFrame API.
 *
 * Overview:
 * - **RDD to DataFrame Conversion**: Begins by converting an RDD of strings into a DataFrame. This
 *   transformation facilitates more complex operations and optimizations that DataFrames support over RDDs.
 *
 * - **Data Manipulation and Aggregation**: Implements a text processing operation where strings are split
 *   into words, and then performs an aggregation to count the occurrences of each word. This is achieved
 *   using DataFrame operations that are SQL-like, making them more readable and easier to understand compared
 *   to traditional RDD operations.
 *
 * - **Explode and Split Functions**: Demonstrates the use of `explode` and `split` functions from the Spark
 *   SQL API to flatten and transform the data structure, allowing for row-wise transformations and aggregations.
 *
 * Use Cases:
 * - This class serves as an educational tool for understanding how to transition from RDDs to DataFrames and
 *   utilize Spark's built-in SQL functions for data manipulation and analysis.
 * - It can be adapted for data preprocessing tasks in analytics pipelines where textual data needs to be parsed,
 *   transformed, and summarized efficiently.
 */
object DataFrameOperations extends App {
  import spark.implicits._

  val rddStrings = spark.sparkContext.parallelize(Seq("hola mundo", "hola", "mundo", "hola", "hola"))
  val df = rddStrings.toDF("linea")
  df.show()

  df.selectExpr("explode(split(linea, ' ')) as palabra").groupBy("palabra").count().show()

  SparkSetup.stop()
}