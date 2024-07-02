package week2.sparksql.olap


import week2.sparksql.SparkSessionWrapper
import org.apache.spark.sql.functions

/**
 * A Scala application to demonstrate the usage of grouping sets and rollup in Apache Spark SQL.
 * This example uses both SQL and DataFrame APIs to perform similar data aggregation operations
 * to help illustrate how grouping sets can be used to generate various levels of summaries.
 */
object GroupingSets01App extends App with SparkSessionWrapper {

  // Set the Spark log level to ERROR to reduce the amount of logs displayed.
  spark.sparkContext.setLogLevel("ERROR")

  // Disable Adaptive Query Execution (AQE) to ensure consistent query plans for demonstration.
  spark.conf.set("spark.sql.adaptive.enabled", "false")

  // Disable automatic broadcast join to force Spark to plan other types of joins.
  spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "-1")

  import spark.implicits._

  // Sample data representing sales records
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

  // Create a DataFrame from the sample data
  val df = data.toDF("Name", "Category", "Sales", "Quantity")

  // Register the DataFrame as a temporary view to use SQL queries
  df.createOrReplaceTempView("sales_data")

  // Perform aggregation using SQL with grouping sets to sum sales by Name and Category
  val result = spark.sql("""
    SELECT Name, Category, sum(Sales) as TotalSales
    FROM sales_data
    GROUP BY Name, Category
    GROUPING SETS ((Name, Category))
  """)

  // Alternative method using DataFrame API with rollup to obtain similar results
  val resultDf = df
    .groupBy("Name", "Category")
    .agg(functions.sum("Sales").alias("TotalSales"))
    .rollup("Name", "Category")
    .agg(functions.sum("TotalSales").alias("TotalSales"))

  // Explain the physical plan of both queries to compare
  result.explain(true)
  resultDf.explain(true)

  // Show the results of both queries
  resultDf.show(false)
  result.show(false)
}
