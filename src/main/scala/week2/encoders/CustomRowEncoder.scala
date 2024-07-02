package week2.encoders

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Encoder, Row, SparkSession}

import scala.reflect.{ClassTag, classTag}

object CustomRowEncoder extends App {

  // Definir una clase para representar datos de personas con atributos personalizados
  final case class Person(id: Int, name: String, age: Int, metadata: Map[String, String])

  // Crear una instancia de SparkSession
  val spark = SparkSession.builder().master("local").appName("CustomEncoderExample").getOrCreate()

  // Crear un DataFrame a partir de una secuencia de objetos Person
  val persons = Seq(
    Person(1, "Alice", 30, Map("city" -> "New York", "gender" -> "Female")),
    Person(2, "Bob", 25, Map("city" -> "San Francisco", "gender" -> "Male"))
  )
  val personsDF = spark.createDataFrame(persons)

  // Definir un encoder personalizado para la clase Person
  val customEncoder: Encoder[Person] = ExpressionEncoder()

  // Crear una función para convertir un objeto Person en una fila con los atributos personalizados
  val customRowEncoder: Encoder[Person] = new Encoder[Person] {
    def schema: StructType = {
      StructType(
        StructField("id", IntegerType, nullable = false) ::
          StructField("name", StringType, nullable = false) ::
          StructField("age", IntegerType, nullable = false) ::
          StructField("metadata", StringType, nullable = true) :: Nil
      )
    }

    def clsTag: ClassTag[Person] = classTag[Person]

    def toRow(person: Person): Row = {
      Row(person.id, person.name, person.age, person.metadata.toString)
    }

    def fromRow(row: Row): Person = {
      val id = row.getInt(0)
      val name = row.getString(1)
      val age = row.getInt(2)
      val metadataString = row.getString(3)
      val metadataMap = metadataString
        .stripPrefix("{")
        .stripSuffix("}")
        .split(",")
        .map(_.split(":"))
        .map(parts => parts(0).trim -> parts(1).trim)
        .toMap
      Person(id, name, age, metadataMap)
    }
  }

  // Aplicar el encoder personalizado para convertir el DataFrame en un Dataset tipado
  private val personsDS = personsDF.as(customRowEncoder)

  // Realizar operaciones con el Dataset
  private val filteredPersons = personsDS.filter(person => person.age > 25)

  // Mostrar los resultados
  filteredPersons.show()

}
