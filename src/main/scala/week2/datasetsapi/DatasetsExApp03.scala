package week2.datasetsapi

import org.apache.spark.sql.DataFrame

/**
 * Demonstrates the conversion between DataFrames and Datasets using a simple case class.
 */
object DatasetsExApp03 extends App {
  // Example of converting DataFrames to Datasets and vice versa
  // Necessary classes must be imported

  import org.apache.spark.sql.SparkSession

  /**
   * Case class to represent a row of data, modelling a person with a name and age.
   */
  case class Person(name: String, age: Int)

  /**
   * Create a SparkSession with a local master configuration for demonstration purposes.
   */
  val spark = SparkSession.builder().appName("EjemploDatasets03App")
    .master("local[2]").getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")  // Set log level to ERROR to minimize console output

  // Create a DataFrame from a sequence of Person objects
  val data = Seq(Person("Alice", 34), Person("Bob", 45), Person("Charlie", 23))
  val df: DataFrame = spark.createDataFrame(data)
  df.show(false)  // Display the DataFrame contents without truncating columns

  // Convert the DataFrame to a Dataset of Person instances
  import spark.implicits._
  val ds = df.as[Person]
  ds.show(false)  // Display the Dataset contents without truncating columns
}
