package week2.sparksql.otherusecases

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import java.time.Instant
import scala.util.Random

/**
 * Object to generate and manage simulated IoT device data using Apache Spark.
 *
 * This object handles the creation of simulated IoT data and demonstrates the use of Spark's DataFrame API
 * to manage large datasets efficiently. The data is generated, transformed, and stored in JSON format.
 */
object IoTDataGenerator {
  // Path where the generated IoT data in JSON format will be saved.
  val IotJsonDataPath = "week2/sparksql/otherusecases/data/datagen/iot_data.json"

  // Defined range for generating random temperature values.
  val ValorMinimoTemperatura: Double = -20
  val ValorMaximoTemperatura: Double = 50

  // Defined range for generating random humidity values.
  val ValorMinimoHumedad: Double = 0
  val ValorMaximoHumedad: Double = 100

  // Case class representing a structured IoT data record.
  case class IotData(deviceId: Int, deviceType: String, timestamp: Long, temperature: Double, humidity: Double)

  // List of possible device types.
  val deviceTypes = List("thermostat", "moisture_sensor", "light_detector")

  /**
   * Generates a random integer within a specified range.
   *
   * @param start the lower bound of the range (inclusive).
   * @param end the upper bound of the range (inclusive).
   * @return a randomly selected integer between the start and end bounds.
   */
  def generateRandomInt(start: Int, end: Int): Int = {
    start + Random.nextInt((end - start) + 1)
  }

  /**
   * Generates a random Double within a specified range.
   *
   * @param start the lower bound of the range (inclusive).
   * @param end the upper bound of the range (inclusive).
   * @return a randomly selected Double between the start and end bounds.
   */
  private def generateRandomDouble(start: Double, end: Double): Double = {
    start + Random.nextDouble() * (end - start)
  }

  /**
   * Generates an IoT data record using the defined ranges and types.
   *
   * Utilizes the previously defined methods for generating random integers and doubles,
   * combines these values with a timestamp and randomly chosen device type to form a structured IoT data record.
   *
   * @return an instance of IotData containing simulated device information.
   */
  def generateIotData(): IotData = {
    val deviceId = generateRandomInt(1, 1000)
    val deviceType = deviceTypes(Random.nextInt(deviceTypes.size))
    val timestamp = Instant.now.getEpochSecond
    val temperature = generateRandomDouble(ValorMinimoTemperatura, ValorMaximoTemperatura)
    val humidity    = generateRandomDouble(ValorMinimoHumedad, ValorMaximoHumedad)

    IotData(deviceId, deviceType, timestamp, temperature, humidity)
  }

  /**
   * Main method to orchestrate the generation, processing, and storage of IoT data.
   *
   * The method initializes a Spark session, generates a sequence of IoT data,
   * converts it to a DataFrame, and finally writes this DataFrame to a JSON file.
   * It demonstrates data aggregation, partitioning, and schema inference capabilities of Spark.
   *
   * @param args command-line arguments (not used).
   */
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("IoT Data Generator")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    import spark.implicits._

    val iotDataSeq: Seq[IotData] = (1 to 10000).map(_ => generateIotData())
    val iotDataDF: DataFrame = iotDataSeq.toDF()

    iotDataDF
      .coalesce(2)  // Reduces the number of partitions to 2 to optimize file writing.
      .write
      .mode(SaveMode.Overwrite)  // Overwrites existing data or creates new if none exists.
      .json(IotJsonDataPath)  // Writes the data to a JSON file specified by the path.

    val dfJson = spark.read.json(IotJsonDataPath)
    dfJson.show()  // Displays the first few records of the DataFrame.
    dfJson.printSchema()  // Prints the schema inferred from the JSON data.
    println(dfJson.count())  // Prints the number of records in the DataFrame.
    spark.stop()  // Stops the Spark session.
  }
}
