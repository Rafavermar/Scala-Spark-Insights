package week2.sparksql.otherusecases


import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * A utility object to run SQL queries in Spark.
 *
 * This object encapsulates functionality to execute SQL queries from a string,
 * allowing for setting debugging properties and other contextual information within Spark's session.
 */
object SQLRunner {

  /**
   * Executes an SQL query provided as a string and allows debugging and job description settings.
   *
   * @param query The SQL query to be executed.
   * @param sqlCount An identifier for the SQL call, typically used for tracking query counts.
   * @param withDebug Boolean flag to turn debug settings on or off.
   * @param componentName Name of the component or the context in which this SQL is being run.
   * @param spark Implicit SparkSession which provides the context for SQL execution.
   * @return DataFrame resulting from the SQL query execution.
   */
  def runSqlFromString(query: String, sqlCount: Int, withDebug: Boolean = true, componentName: String = " ")(implicit spark: SparkSession): DataFrame = {
    spark.conf.set("spark.sql.execution.debug", withDebug)
    spark.sparkContext.setLocalProperty("callSite.short", s"SQL_$sqlCount")
    spark.sparkContext.setLocalProperty("callSite.long", s"SQL -> $query")
    spark.sparkContext.setJobDescription(s"$componentName $sqlCount: $query")
    spark.sql(query)
  }
}

/**
 * Demonstrates the usage of SqlRunner by executing SQL queries from strings.
 */
object TestSqlRunner extends App {
  // Initialize SparkSession with application name and master settings
  val spark: SparkSession = SparkSession.builder()
    .appName("SqlRunnerTest")
    .master("local[*]")
    .getOrCreate()

  // Import implicit conversions and functions
  import spark.implicits._

  // Create a dummy DataFrame to act as a database table
  val df: DataFrame = Seq(
    (1, "John"),
    (2, "Martin"),
    (3, "Linda")
  ).toDF("id", "name")

  // Register the DataFrame as a temporary view to use in SQL queries
  df.createOrReplaceTempView("People")

  // Example SQL queries to run using SqlRunner
  val query: String = "SELECT * FROM People"
  val query2: String = "SELECT id FROM People WHERE name = 'Martin'"
  val componentName: String = "TestSqlRunner"

  // Execute the first query and display the results
  SQLRunner.runSqlFromString(query = query, sqlCount = 1, componentName = componentName)(spark)
    .show(truncate = false)

  // Execute the second query and display the results
  SQLRunner.runSqlFromString(query2, sqlCount = 2, componentName = componentName)(spark)
    .show(truncate = false)

  // Pausing the application to observe Spark UI at http://localhost:4040
  Thread.sleep(200000)

  // Stop the Spark session to release resources
  spark.stop()
}
