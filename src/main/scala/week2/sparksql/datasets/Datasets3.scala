package week2.sparksql.datasets


import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * This object demonstrates how to convert DataFrames to Datasets and vice versa,
 * utilizing Spark SQL's powerful capabilities for type-safe data operations.
 */
object Datasets3 extends App {

  // Case class defined to represent a row of data in a dataset
  case class Person(name: String, age: Int)

  // Creating a Spark session, which is the entry point to programming Spark with the Dataset and DataFrame API.
  val spark = SparkSession.builder()
    .appName("Datasets3")
    .master("local[2]") // use local mode with 2 cores
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR") // Set log level to reduce verbosity of Spark output

  import spark.implicits._

  // Creating a DataFrame from a sequence of Person objects.
  // A DataFrame is a distributed collection of data organized into named columns.
  val data = Seq(Person("Alice", 34), Person("Bob", 45), Person("Charlie", 23))
  val df: DataFrame = spark.createDataFrame(data)

  // Displaying the DataFrame to the console
  df.show(false)

  // Converting the DataFrame to a Dataset using the case class Person.
  // Datasets are strongly-typed, meaning that they are aware of the Scala data types they contain.
  val ds: Dataset[Person] = df.as[Person]

  // Displaying the Dataset to the console
  ds.show(false)

  spark.stop() // Stopping the Spark session
}
