package week2.encoders

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object PersonTypes {

  type PersonName = String
  type PersonAge = Int
  type ActivityName = String
  type WhenIsDoneT = String

  final case class Activity(activityName: ActivityName) extends AnyVal

  final case class WhenIsDone(when: WhenIsDoneT) extends AnyVal

  type LikesDoing = Map[Activity, WhenIsDone]
  type OptLikesDoing = Option[LikesDoing]

  sealed trait HumanBeing {
    def name: PersonName

    def age: PersonAge

    def likesDoing: OptLikesDoing
  }

  val drinkBeer: Activity = Activity("DrinkBeer")
  val doNothing: Activity = Activity("NoActivity")
  val daily: WhenIsDone = WhenIsDone("Daily")
  val always: WhenIsDone = WhenIsDone("Always")
  val drinkBeerDaily: OptLikesDoing = Some(Map(drinkBeer -> daily))
  val alwaysDoNothing: OptLikesDoing = Some(Map(doNothing -> always))


  final case class Friend(name: PersonName, age: PersonAge, likesDoing: OptLikesDoing = drinkBeerDaily) extends HumanBeing

  final case class Person(name: PersonName, age: PersonAge, friends: List[Friend], likesDoing: OptLikesDoing = alwaysDoNothing) extends HumanBeing
}

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

  private def registerKryoClasses(spark: SparkSession): Unit = {
    import PersonTypes._
    spark.sparkContext.getConf.registerKryoClasses(Array(classOf[Person], classOf[Friend]))
  }
}

object Encoders3 extends App {

  private def run(): Unit = {
    val spark: SparkSession = SparkSessionFactory.create()
    val df: DataFrame = createDataFrame(spark)
    operateOnDF(df)(writeToParquet)
    df.show(false)
  }

  def createDataFrame(spark: SparkSession): DataFrame = {
    import PersonTypes._
    import spark.implicits._
    Seq(
      Person("John", 30, List(Friend("Jim", 31), Friend("Mike", 35))),
      Person("Amy", 25, List(Friend("Sarah", 26), Friend("Sue", 28)))
    ).toDF()
  }

  private def writeToParquet(df: DataFrame): Unit = {
    df.write.mode(SaveMode.Overwrite).parquet("persons_kryo.parquet")
  }

  private def operateOnDF(df: DataFrame)(f: DataFrame => Unit): Unit = {
    f(df)
  }

  run()
}

object MyPersonDSL {

  import PersonTypes._

  implicit class PersonDSL(person: Person) {
    def likes(activity: Activity): Person = {
      person.copy(likesDoing = Some(Map(activity -> always)))
    }

    def knows(friend: Friend): Person = {
      person.copy(friends = friend :: person.friends)
    }

    def ageIs(age: Int): Person = {
      person.copy(age = age)
    }
  }

  def person(name: PersonName): Person = Person(name, 0, List.empty, None)

  def friend(name: PersonName): Friend = Friend(name, 0, None)

  def activity(name: ActivityName): Activity = Activity(name)
}

object MyPersonDSLApp extends App {

  import MyPersonDSL._
  import PersonTypes._

  private val playFootbal = activity("PlayFootball")
  val john = person("John")
    .ageIs(30)
    .likes(drinkBeer)
    .likes(playFootbal)
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