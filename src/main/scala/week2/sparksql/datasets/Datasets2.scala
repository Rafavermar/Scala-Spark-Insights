package week2.sparksql.datasets


import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}

import scala.language.implicitConversions

/**
 * Advanced usage of Datasets in Spark with complex data structures.
 * Shows the usage of nested datasets, tuples, lists, maps, and options.
 */
object Datasets2 extends App {

  implicit val spark: SparkSession = SparkSession.builder()
    .appName("EjemploDatasets02App").master("local[2]").getOrCreate()

  /**
   * Examples of using advanced Datasets with complex data structures
   * Nested datasets
  */
  case class Person(name: String, age: Int, address: Address)

  case class Address(city: String, state: String)

  val address1 = Address("Los Angeles", "California")
  val address2 = Address("New York", "New York")
  val address3 = Address("Chicago", "Illinois")

  val data = Seq(
    Person("Alice", 34, address1),
    Person("Bob", 45, address2),
    Person("Charlie", 23, address3),
  )

  // Create an Encoder for the Person class
  // The encoder is used to serialize and deserialize objects of type Person
  implicit val personEncoder: Encoder[Person] = Encoders.product[Person]

/**
 * Create a Dataset from a sequence of Person objects
 * This is the biggest change from DataFrames, since DataFrames are not typed
 * toDS() is an implicit SparkSession method that converts a sequence of objects to a Dataset
 */
  import spark.implicits._

  val ds = data.toDS()
  ds.show(false)

  /**
   * Tuples Datasets
  */
  case class PersonTuple(name: String, age: Int, address: (String, String))

  val dataTuple = Seq(
    PersonTuple("Alice", 34, ("Los Angeles", "California")),
    PersonTuple("Bob", 45, ("New York", "New York")),
    PersonTuple("Charlie", 23, ("Chicago", "Illinois")),
  )


  // Create an Encoder for the PersonTuple class
  // The encoder is used to serialize and deserialize objects of type PersonTuple

  implicit val personTupleEncoder: Encoder[PersonTuple] = Encoders
    .product[PersonTuple]

  val dsTuple = dataTuple.toDS()
  dsTuple.show(false)

  /**
   *  Nested tuple datasets
  */
  case class PersonNested(name: String, age: Int, address: Address)

  val dataNested = Seq(
    PersonNested("Alice", 34, address1),
    PersonNested("Bob", 45, address2),
    PersonNested("Charlie", 23, address3),
  )

   // Create an Encoder for the PersonNested class

  implicit val personNestedEncoder: Encoder[PersonNested] = Encoders
    .product[PersonNested]

  /**
   * List datasets
   */

  val personList1: Seq[Person] =
    List(Person("Alice", 34, address1), Person("Bob", 45, address2))
  val personList2: Seq[Person] = List(Person("Charlie", 23, address3))
  val dataLists: Seq[Seq[Person]] = List(personList1, personList2)


   // Create an Encoder for the List[Person] class

  // implicit val personListEncoder: Encoder[Seq[Person]] = Encoders.product[Seq[Person]]
  implicit val personListEncoder: Encoder[Seq[Person]] = Encoders
    .kryo[Seq[Person]]
  // Kryo uses binary serialization, which is more efficient than the default Java serialization but does not support all data types.
  // It also cannot be read directly by humans because it is binary.

  val dsLists = dataLists.toDS()
  dsLists.show(false)
  dsLists.printSchema()

  val listaDs1 = personList1.toDS()
  listaDs1.show(false)
  listaDs1.printSchema()
  val listaDs2 = personList2.toDS()
  listaDs2.show(false)
  listaDs2.printSchema()


  /**
   * Maps Datasets
   */

  val dataMaps = Seq(
    Map("name" -> "Alice", "age" -> 34, "address" -> address1),
    Map("name" -> "Bob", "age" -> 45, "address" -> address2),
    Map("name" -> "Charlie", "age" -> 23, "address" -> address3),
  )

  // Create an Encoder for the Map[String, Any] class
  implicit val mapEncoder: Encoder[Map[String, Any]] = Encoders
    .kryo[Map[String, Any]]

  // val dsMaps: Dataset[Map[String, Any]] = dataMaps.toDS()

  /**
   *  Nested map datasets
   * @param name
   * @param age
   * @param address
   */
   case class PeopleWithAddress(name: String, age: Int, address: Address)

  val dataPeopleWithAddress: Seq[PeopleWithAddress] = Seq(
    PeopleWithAddress("Alice", 34, address1),
    PeopleWithAddress("Bob", 45, address2),
    PeopleWithAddress("Charlie", 23, address3),
  )
  val dsPeopleWithAddress: Dataset[PeopleWithAddress] =
    dataPeopleWithAddress.toDS()
  dsPeopleWithAddress.show(false)

  val dataMapsNested: Seq[Map[String, Any]] = Seq(
    Map(
      "name" -> "Alice",
      "age" -> 34,
      "address" -> Map("city" -> "Los Angeles", "state" -> "California"),
    ),
    Map(
      "name" -> "Bob",
      "age" -> 45,
      "address" -> Map("city" -> "New York", "state" -> "New York"),
    ),
    Map(
      "name" -> "Charlie",
      "age" -> 23,
      "address" -> Map("city" -> "Chicago", "state" -> "Illinois"),
    ),
  )

  // Create an Encoder for the Map[String, Any] class
  implicit val mapNestedEncoder: Encoder[Map[String, Any]] = Encoders
    .kryo[Map[String, Any]]

  // Map[String, Any] is a very flexible data type that can contain any type of value
  // but since it is not deterministic, Spark cannot infer the schema automatically

  /// val dsMapsNested: Dataset[Map[String, Any]] = dataMapsNested.toDS()


  /**
   * Sets Datasets
   */

  type NombresDeAmigos = Set[String]
  val dataSets: Seq[NombresDeAmigos] = Seq(
    Set("Alice", "Bob", "Charlie"),
    Set("David", "Eve", "Frank"),
    Set("George", "Helen", "Ivan"),
  )

  // Create an Encoder for the Set[String] class
  implicit val setEncoder: Encoder[NombresDeAmigos] = Encoders
    .kryo[NombresDeAmigos]

  val dsSets: Dataset[NombresDeAmigos] = dataSets.toDS()
  dsSets.show(false)
  dsSets.printSchema()
  dsSets.take(10).foreach(println)

  // Implicit function to convert a Set[String] to a Dataset
  implicit def setToDataset(set: Set[String]): Dataset[String] = set.toSeq.toDS()

  val dsSets2: Dataset[String] = dataSets.toDS().flatMap(set => set)
  dsSets2.show(false)
  dsSets2.printSchema()
  dsSets2.take(10).foreach(println)

  implicit class ShowDataset[T <: Any](ds: Dataset[T]) {
    def showDS(numRecords: Int = 20, truncate: Boolean = false): Unit = ds
      .take(numRecords).foreach(record => println(record))
  }

  println("dsSets show")
  // The showDS() method of the ShowDataset class is used here
  dsSets.showDS()


  /**
   * Nested Sets Datasets
   */

  type NombresDeAmigosAnidados = Set[Set[String]]
  val dataSetsAnidados: Seq[NombresDeAmigosAnidados] = Seq(
    Set(Set("Alice", "Bob"), Set("Charlie", "David")),
    Set(Set("Eve", "Frank"), Set("George", "Helen")),
    Set(Set("Ivan", "Jack"), Set("Kate", "Liam")),
  )

  // Create an Encoder for the class Set[Set[String]]
  implicit val setAnidadoEncoder: Encoder[NombresDeAmigosAnidados] = Encoders
    .kryo[NombresDeAmigosAnidados]

  val dsSetsAnidados: Dataset[NombresDeAmigosAnidados] = dataSetsAnidados.toDS()
  dsSetsAnidados.printSchema()
  dsSetsAnidados.showDS()

  /**
   * Sequence datasets
   */

  type NombresDeAmigosSeq = Seq[String]
  val dataSeq: Seq[NombresDeAmigosSeq] = Seq(
    Seq("Alice", "Bob", "Charlie"),
    Seq("David", "Eve", "Frank"),
    Seq("George", "Helen", "Ivan"),
  )

  // Create an Encoder for the Seq[String] class
  implicit val seqEncoder: Encoder[NombresDeAmigosSeq] = Encoders
    .kryo[NombresDeAmigosSeq]

  val dsSeq: Dataset[NombresDeAmigosSeq] = dataSeq.toDS()
  dsSeq.showDS(10)
  dsSeq.printSchema()
  // Datasets de secuencias anidadas

  type NombresDeAmigosSeqAnidados = Seq[Seq[String]]
  val dataSeqAnidados: Seq[NombresDeAmigosSeqAnidados] = Seq(
    Seq(Seq("Alice", "Bob"), Seq("Charlie", "David")),
    Seq(Seq("Eve", "Frank"), Seq("George", "Helen")),
    Seq(Seq("Ivan", "Jack"), Seq("Kate", "Liam")),
  )

  // Create an Encoder for the class Seq[Seq[String]]
  implicit val seqAnidadoEncoder: Encoder[NombresDeAmigosSeqAnidados] = Encoders
    .kryo[NombresDeAmigosSeqAnidados]

  val dsSeqAnidados: Dataset[NombresDeAmigosSeqAnidados] = dataSeqAnidados.toDS()
  dsSeqAnidados.printSchema()
  dsSeqAnidados.showDS()


  /**
   * Options Datasets
   */

  type OpcionDeDireccion = Option[Address]
  val dataOptions: Seq[OpcionDeDireccion] =
    Seq(Some(address1), Some(address2), Some(address3), None)

  // Create an Encoder for the Option[Address] class
  implicit val optionEncoder: Encoder[OpcionDeDireccion] = Encoders
    .kryo[OpcionDeDireccion]

  val dsOptions: Dataset[OpcionDeDireccion] = dataOptions.toDS()
  dsOptions.showDS(10)
  dsOptions.printSchema()

  /**
   * Nested options datasets
   * Option[Option[Address]] can be a useful data type to represent data that may or may not be present
   * Opion[Option[T]] is a data type that can have two values: Some(Some(value)) or None and is used to represent nested data
   */

  type OpcionDeDireccionAnidada = Option[Option[Address]]
  val dataOptionsAnidadas: Seq[OpcionDeDireccionAnidada] =
    Seq(Some(Some(address1)), Some(Some(address2)), Some(Some(address3)), None)

  // Create an Encoder for the Option[Option[Address]] class
  implicit val optionAnidadoEncoder: Encoder[OpcionDeDireccionAnidada] =
    Encoders.kryo[OpcionDeDireccionAnidada]

  val dsOptionsAnidadas: Dataset[OpcionDeDireccionAnidada] =
    dataOptionsAnidadas.toDS()
  dsOptionsAnidadas.showDS(10)
  dsOptionsAnidadas.printSchema()

  /**
   * Other examples of advanced functional data processing datasets
   * Things that can be performed with Datasets that are not able to be performed with DataFrames
   */

  /**
   * Tuple sequence datasets
   */

  val dataTuples: Seq[(String, Int, Address)] =
    Seq(("Alice", 34, address1), ("Bob", 45, address2), ("Charlie", 23, address3))

  // Create an Encoder for the class (String, Int, Address)
  implicit val tupleEncoder: Encoder[(String, Int, Address)] = Encoders
    .kryo[(String, Int, Address)]

  val dsTuples: Dataset[(String, Int, Address)] = dataTuples.toDS()
  dsTuples.showDS(10)
  dsTuples.printSchema()

  /**
   * Is it better to use a case class instead of a tuple to represent structured data?
   * Tuples are more efficient than case classes because they do not require the creation of additional objects.
   * Tuples are easier to use than case classes because they do not require the definition of additional classes.
   * Tuples are more flexible than case classes because they can contain any type of data.
   * Tuples are safer than case classes because they do not allow the creation of null objects.
   * Tuples are easier to serialize than case classes because they do not require the definition of additional encoders.
   * Tuples are easier to deserialize than case classes because they do not require the definition of additional decoders.
   * Tuples are easier to read than case classes because they do not require the definition of additional methods.
   * Tuples are easier to write than case classes because they do not require the definition of additional methods.
   * Tuples are easier to debug than case classes because they do not require the definition of additional methods.
   */


  /**
   *  Nested tuple sequences datasets
   */
  val dataTuplesAnidadas: Seq[((String, Int), Address)] = Seq(
    (("Alice", 34), address1),
    (("Bob", 45), address2),
    (("Charlie", 23), address3),
  )

  // Create an Encoder for the class ((String, Int), Address)
  implicit val tupleAnidadoEncoder: Encoder[((String, Int), Address)] = Encoders
    .kryo[((String, Int), Address)]

  val dsTuplesAnidadas: Dataset[((String, Int), Address)] =
    dataTuplesAnidadas.toDS()

  dsTuplesAnidadas.showDS(10)
  dsTuplesAnidadas.printSchema()

  /**
   * nested tuple sequences with complex data types Datasets
   */

  type Nombre = String
  type Edad = Int
  type NombreEdad = (Nombre, Int)
  type PersonTupleAnidado = ((Nombre, Edad, Address), (Nombre, Edad, Address))
  val dataTuplesAnidadasComplejas
  : Seq[((Nombre, Edad, Address), (Nombre, Edad, Address))] = Seq(
    (("Alice", 34, address1), ("Bob", 45, address2)),
    (("Charlie", 23, address3), ("David", 56, address1)),
    (("Eve", 45, address2), ("Frank", 34, address3)),
  )

  // Create an Encoder for the class ((String, Int, Address), (String, Int, Address))
  implicit val tupleAnidadoComplejoEncoder
  : Encoder[((Nombre, Edad, Address), (Nombre, Edad, Address))] = Encoders
    .kryo[((Nombre, Edad, Address), (Nombre, Edad, Address))]

  val dsTuplesAnidadasComplejas
  : Dataset[((Nombre, Edad, Address), (Nombre, Edad, Address))] =
    dataTuplesAnidadasComplejas.toDS()

  dsTuplesAnidadasComplejas.showDS(10)
  dsTuplesAnidadasComplejas.printSchema()

  spark.stop()
}
