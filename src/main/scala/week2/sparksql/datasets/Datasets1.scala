package week2.sparksql.datasets

import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}

/** Case class for representing a person with name and age attributes */
case class Person(name: String, age: Int)

/**
 * Usage of Datasets in Spark, which provide a typed way to handle data.
 * Unlike DataFrames, Datasets allow you to work with data in a type-safe manner.
 */
object Datasets1 extends App {

  /**
   * Creates a SparkSession, which is the entry point to using Spark functionality.
   * `local[2]` master means that the application will run locally using two threads.
   */
  val spark = SparkSession.builder()
    .appName("Datasets1")
    .master("local[2]")
    .getOrCreate()

  // Set log level to ERROR to minimize console output for clarity of example
  spark.sparkContext.setLogLevel("ERROR")

  // Import Spark implicits for automatic conversions like converting RDDs to DataFrames
  import spark.implicits._

  /** Encoder for the Person case class to assist with serialization and deserialization */
  implicit val personEncoder: Encoder[Person] = Encoders.product[Person]

  /**
   * Creating a Dataset of Person objects.
   * `toDS` converts a Scala collection to a Dataset using the implicitly available encoder.
   */
  val data = Seq(Person("Alice", 34), Person("Bob", 45), Person("Charlie", 23))
  val ds = data.toDS()

  // Display the Dataset
  ds.show(false)

  /**
   * Filtering the Dataset using a Scala function.
   * This function is type-safe, i.e., it will compile only if the fields and types are correct.
   */
  val mayoresDeVeintidosFunc: Person => Boolean = p => p.age > 22
  val mayoresDeVeintidos: Dataset[Person] = ds.filter(mayoresDeVeintidosFunc)
  mayoresDeVeintidos.show(false)

  /**
   * Transforming the Dataset by incrementing the age of each Person.
   * This map operation is also type-safe.
   */
  val ds2 = ds.map(p => Person(p.name, p.age + 1))
  ds2.show(false)

  /**
   * A simple function to double the age, demonstrating how to use Scala functions with map.
   */
  val doblarEdad: Int => Int = (age: Int) => age * 2
  val ds3: Dataset[Person] = ds.map(p => Person(p.name, doblarEdad(p.age)))
  ds3.show(false)

  // Stop the Spark session
  spark.stop()
}
