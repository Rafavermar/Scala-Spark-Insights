package week2.sparkapp

import org.apache.spark.SparkConf
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.SparkSession

object FirstSparkAppCong {

  val sparkConfLocal: SparkConf = new SparkConf()
    .setAppName("PrimeraAppSparkConf").setMaster("local[*]")
    .set("spark.shuffle.partitions", "5")
    .set("spark.sql.shuffle.partitions", "5").set("spark.driver.memory", "2g")
    .set("spark.driver.cores", "1").set("spark.executor.memory", "2g")
    .set("spark.executor.cores", "2").set("spark.executor.instances", "2")

  val sparkConfTesting: SparkConf = new SparkConf().setAppName("PrimeraAppSparkConf")
    .setMaster("local[2]").set("spark.shuffle.partitions", "5")
    .set("spark.sql.shuffle.partitions", "5").set("spark.driver.memory", "512m")
    .set("spark.driver.cores", "1").set("spark.executor.memory", "512m")
    .set("spark.executor.cores", "1").set("spark.ui.enabled", "false")
    .set("spark.sql.autoBroadcastJoinThreshold", "-1")
    .set("spark.dirs", "/tmp/spark-temp")

}

object PrimeraAppSparkDataGen {
  private val maxAge = 75
  def generateData(numRecords: Int): Seq[(String, Int)] = {
    val data = (1 to numRecords).map { i =>
      val name = s"Name$i"
      val age = scala.util.Random.nextInt(maxAge)
      (name, age)
    }
    data
  }
}

object FirstSparkApp extends App {

  val spark = SparkSession.builder().master("local[*]")
    .appName("PrimeraAppSpark").getOrCreate()

  import spark.implicits._

  val data: Seq[(String, Int)] = PrimeraAppSparkDataGen.generateData(100000)
  val df: DataFrame = data.toDF("name", "age")

  df.show(truncate = false)

  val mayoresDeVeintidos: DataFrame = df.filter($"age" > 22)
  mayoresDeVeintidos.show(truncate = false)
  mayoresDeVeintidos.write.mode(SaveMode.Overwrite)
    .csv("out/mayoresDeVeintidos.csv")

  spark.stop()
}
