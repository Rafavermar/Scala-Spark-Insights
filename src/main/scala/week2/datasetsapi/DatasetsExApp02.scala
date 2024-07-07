package week2.datasetsapi

import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

import scala.language.implicitConversions

/**
 * Demonstrates advanced usage of Datasets in Spark with complex data structures including nested datasets,
 * tuples, and collections.
 */
object DatasetsExApp02 extends App {

  // Implicitly create a Spark session configured for local execution
  implicit val spark: SparkSession = SparkSession.builder()
    .appName("EjemploDatasets02App").master("local[2]").getOrCreate()

  // Define a case class to represent a person with nested structure
  case class Person(name: String, age: Int, address: Address)
  case class Address(city: String, state: String)

  // Sample addresses
  val address1 = Address("Los Angeles", "California")
  val address2 = Address("New York", "New York")
  val address3 = Address("Chicago", "Illinois")

  // Sample data for persons
  val data = Seq(
    Person("Alice", 34, address1),
    Person("Bob", 45, address2),
    Person("Charlie", 23, address3),
  )

  // Implicit encoder for the Person case class
  implicit val personEncoder: Encoder[Person] = Encoders.product[Person]

  // Create a Dataset from a sequence of Person objects
  import spark.implicits._
  val ds = data.toDS()
  ds.show(false)

  // Example with tuples to represent persons
  case class PersonTuple(name: String, age: Int, address: (String, String))
  val dataTuple = Seq(
    PersonTuple("Alice", 34, ("Los Angeles", "California")),
    PersonTuple("Bob", 45, ("New York", "New York")),
    PersonTuple("Charlie", 23, ("Chicago", "Illinois")),
  )

  // Implicit encoder for PersonTuple
  implicit val personTupleEncoder: Encoder[PersonTuple] = Encoders.product[PersonTuple]

  val dsTuple = dataTuple.toDS()
  dsTuple.show(false)

  // Nested structure for persons using the same address case class
  case class PersonNested(name: String, age: Int, address: Address)
  val dataNested = Seq(
    PersonNested("Alice", 34, address1),
    PersonNested("Bob", 45, address2),
    PersonNested("Charlie", 23, address3),
  )

  // Implicit encoder for the nested Person case class
  implicit val personNestedEncoder: Encoder[PersonNested] = Encoders.product[PersonNested]

  val dsNested = dataNested.toDS()
  dsNested.show(false)

  // Stop the Spark session at the end of the application
  spark.stop()
}
