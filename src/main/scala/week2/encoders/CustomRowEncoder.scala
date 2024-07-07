package week2.encoders

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Encoder, Row, SparkSession}
import scala.reflect.{ClassTag, classTag}

/**
 * Demonstrates the creation and use of a custom encoder for Spark SQL.
 * This application defines a custom row encoder for a case class `Person`.
 */
object CustomRowEncoder extends App {

  /**
   * Represents a person with customized attributes.
   * @param id Unique identifier of the person.
   * @param name Name of the person.
   * @param age Age of the person.
   * @param metadata Additional metadata about the person as a key-value map.
   */
  final case class Person(id: Int, name: String, age: Int, metadata: Map[String, String])

  /**
   * Initialize SparkSession with a local master configuration.
   * This session is used to manage configurations and perform operations like creating DataFrames.
   */
  val spark = SparkSession.builder().master("local").appName("CustomEncoderExample").getOrCreate()

  /**
   * Create a DataFrame from a sequence of Person objects.
   * This DataFrame serves as the foundation for further operations like custom encoding.
   */
  val persons = Seq(
    Person(1, "Alice", 30, Map("city" -> "New York", "gender" -> "Female")),
    Person(2, "Bob", 25, Map("city" -> "San Francisco", "gender" -> "Male"))
  )
  val personsDF = spark.createDataFrame(persons)

  /**
   * Define an ExpressionEncoder for the Person class to convert Person objects to and from the internal Spark SQL format.
   * This encoder is used implicitly by the dataset operations.
   */
  val customEncoder: Encoder[Person] = ExpressionEncoder()

  /**
   * Custom Encoder to manually convert instances of Person into Spark SQL Rows and back.
   * This allows for control over how each field is encoded into a Row and reconstructed.
   */
  val customRowEncoder: Encoder[Person] = new Encoder[Person] {
    def schema: StructType = StructType(
      StructField("id", IntegerType, nullable = false) ::
        StructField("name", StringType, nullable = false) ::
        StructField("age", IntegerType, nullable = false) ::
        StructField("metadata", StringType, nullable = true) :: Nil
    )

    def clsTag: ClassTag[Person] = classTag[Person]

    def toRow(person: Person): Row = {
      Row(person.id, person.name, person.age, person.metadata.toString)
    }

    def fromRow(row: Row): Person = {
      val id = row.getInt(0)
      val name = row.getString(1)
      val age = row.getInt(2)
      val metadataString = row.getString(3)
      val metadataMap = metadataString.stripPrefix("{").stripSuffix("}").split(",").map(_.split(":")).map(parts => parts(0).trim -> parts(1).trim).toMap
      Person(id, name, age, metadataMap)
    }
  }

  /**
   * Convert the DataFrame to a strongly typed Dataset using the custom row encoder.
   * This enables type-safe operations on the data.
   */
  private val personsDS = personsDF.as[Person](customRowEncoder)

  /**
   * Filter the Dataset to find persons older than 25.
   */
  private val filteredPersons = personsDS.filter(person => person.age > 25)

  /**
   * Display the results of the filter operation.
   */
  filteredPersons.show()
}
