package week2.encoders

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * Defines types and classes related to a simple domain model involving human beings and their activities.
 */
object PersonTypes {

  // Type aliases for readability and domain specificity.
  type PersonName = String
  type PersonAge = Int
  type ActivityName = String
  type WhenIsDoneT = String

  // Represents an activity with a name.
  final case class Activity(activityName: ActivityName) extends AnyVal

  // Represents when an activity is done.
  final case class WhenIsDone(when: WhenIsDoneT) extends AnyVal

  // Map of activities and when they are done.
  type LikesDoing = Map[Activity, WhenIsDone]
  type OptLikesDoing = Option[LikesDoing]

  // Trait defining a human being with optional activities they like.
  sealed trait HumanBeing {
    def name: PersonName
    def age: PersonAge
    def likesDoing: OptLikesDoing
  }

  // Example instances of activities and their timing.
  val drinkBeer: Activity = Activity("DrinkBeer")
  val doNothing: Activity = Activity("NoActivity")
  val daily: WhenIsDone = WhenIsDone("Daily")
  val always: WhenIsDone = WhenIsDone("Always")
  val drinkBeerDaily: OptLikesDoing = Some(Map(drinkBeer -> daily))
  val alwaysDoNothing: OptLikesDoing = Some(Map(doNothing -> always))

  // Concrete human being who is a friend.
  final case class Friend(name: PersonName, age: PersonAge, likesDoing: OptLikesDoing = drinkBeerDaily) extends HumanBeing

  // Concrete human being who may have friends.
  final case class Person(name: PersonName, age: PersonAge, friends: List[Friend], likesDoing: OptLikesDoing = alwaysDoNothing) extends HumanBeing
}

/**
 * Factory object for creating a Spark session configured to use Kryo serialization.
 */
object SparkSessionFactory {
  def create(): SparkSession = {
    val spark = SparkSession.builder()
      .appName("Example 3: Spark Kryo Serialization Case Class With Collection Example")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.kryo.registrationRequired", "true")
      .master("local")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")
    registerKryoClasses(spark)
    spark
  }

  // Registers case classes used in this application with Kryo.
  private def registerKryoClasses(spark: SparkSession): Unit = {
    import PersonTypes._
    spark.sparkContext.getConf.registerKryoClasses(Array(classOf[Person], classOf[Friend]))
  }
}

/**
 * Main application object that uses Spark to manage data.
 */
object Encoders3 extends App {

  private def run(): Unit = {
    val spark: SparkSession = SparkSessionFactory.create()
    val df: DataFrame = createDataFrame(spark)
    operateOnDF(df)(writeToParquet)
    df.show(false)
  }

  // Creates a DataFrame from a sequence of Person instances.
  def createDataFrame(spark: SparkSession): DataFrame = {
    import PersonTypes._
    import spark.implicits._
    Seq(
      Person("John", 30, List(Friend("Jim", 31), Friend("Mike", 35))),
      Person("Amy", 25, List(Friend("Sarah", 26), Friend("Sue", 28)))
    ).toDF()
  }

  // Writes a DataFrame to a Parquet file.
  private def writeToParquet(df: DataFrame): Unit = {
    df.write.mode(SaveMode.Overwrite).parquet("persons_kryo.parquet")
  }

  // Generic function to apply a function to a DataFrame.
  private def operateOnDF(df: DataFrame)(f: DataFrame => Unit): Unit = {
    f(df)
  }

  run()
}

/**
 * DSL (Domain Specific Language) for creating and manipulating person entities.
 */
object MyPersonDSL {

  import PersonTypes._

  // Enhances the Person class with DSL methods.
  implicit class PersonDSL(person: Person) {
    def likes(activity: Activity): Person = person.copy(likesDoing = Some(Map(activity -> always)))
    def knows(friend: Friend): Person = person.copy(friends = friend :: person.friends)
    def ageIs(age: Int): Person = person.copy(age = age)
  }

  // Factory methods for creating persons and friends.
  def person(name: PersonName): Person = Person(name, 0, List.empty, None)
  def friend(name: PersonName): Friend = Friend(name, 0, None)
  def activity(name: ActivityName): Activity = Activity(name)
}

/**
 * Application demonstrating the use of the DSL for person creation and manipulation.
 */
object MyPersonDSLApp extends App {

  import MyPersonDSL._
  import PersonTypes._

  private val playFootball = activity("PlayFootball")
  val john = person("John")
    .ageIs(30)
    .likes(drinkBeer)
    .likes(playFootball)
    .knows(friend("Jim"))
    .knows(friend("Mike"))

  val amy = person("Amy")
    .ageIs(25)
    .likes(doNothing)
    .knows(friend("Sarah"))
    .knows(friend("Sue"))

  println(john)
  println(amy)
}
