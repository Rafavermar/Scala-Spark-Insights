package week2.datasetsapi

import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}

// Definition of the Person case class outside the object to make it globally accessible within this package
case class Person(name: String, age: Int)

/**
 * Demonstrates basic operations on Datasets in Spark, showcasing type safety and functional transformations.
 */
object DatasetsExApp01 extends App {

  // Create an Encoder for the Person class
  // This encoder is used to serialize and deserialize objects of type Person
  implicit val personEncoder: Encoder[Person] = Encoders.product[Person]

  // Initialize a Spark session configured for local execution
  val spark = SparkSession.builder().appName("EjemploDatasets01App")
    .master("local[2]").getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")  // Set log level to ERROR to minimize console output

  import spark.implicits._

  // Create a Dataset from a sequence of Person objects
  // This highlights a major difference from DataFrames, which are not typed
  val data = Seq(Person("Alice", 34), Person("Bob", 45), Person("Charlie", 23))
  val ds = data.toDS()  // toDS() is an implicit method provided by SparkSession that converts a sequence to a Dataset

  ds.show(false)

  // Define a standard Scala function to filter persons older than 22
  val mayoresDeVeintidosFunc: Person => Boolean = (p: Person) => p.age > 22
  val mayoresDeVeintidos: Dataset[Person] = ds.filter(mayoresDeVeintidosFunc)

  mayoresDeVeintidos.show(false)

  // Map operation to increment each person's age by 1
  val ds2 = ds.map(p => Person(p.name, p.age + 1))

  ds2.show(false)

  // Define a function to double the age
  val doblarEdad: Int => Int = (age: Int) => age * 2

  // Apply the function using map to create a new Dataset with doubled ages
  val ds3: Dataset[Person] = ds.map(p => Person(p.name, doblarEdad(p.age)))

  ds3.show(false)

  // Stop the Spark session at the end of application execution
  spark.stop()
}
