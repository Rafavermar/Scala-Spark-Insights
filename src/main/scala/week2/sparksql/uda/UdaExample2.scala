package week2.sparksql.uda

import org.apache.spark.sql.{Encoder, Encoders, SparkSession, TypedColumn}
import org.apache.spark.sql.expressions.Aggregator

/**
 * Case class representing sensor data.
 *
 * @param sensorID Unique identifier for the sensor.
 * @param value Numerical value recorded by the sensor.
 * @param timestamp Time at which the measurement was taken.
 */
case class SensorData(sensorID: String, value: Double, timestamp: Long)

/**
 * Case class representing the aggregation buffer.
 *
 * @param sum Sum of sensor values.
 * @param count Number of values aggregated.
 * @param average Calculated average of the values.
 */
case class AggregateBuffer(sum: Double, count: Int, average: Double)

/**
 * Object containing functions related to IoT data processing.
 */
object UdaExample2 {
  /**
   * An aggregator to calculate the average value of sensor readings.
   * It processes inputs of SensorData and outputs a Double representing the average value.
   */
  object AverageSensorValue extends Aggregator[SensorData, AggregateBuffer, Double] {

    /**
     * Provides a zero value for the aggregation to handle empty data sets.
     */
    def zero: AggregateBuffer = AggregateBuffer(0.0, 0, 0.0)

    /**
     * Adds a new SensorData entry to the aggregation buffer.
     *
     * @param bufferData The current aggregation state.
     * @param data New data to add to the aggregation.
     * @return Updated aggregation buffer.
     */
    def reduce(bufferData: AggregateBuffer, data: SensorData): AggregateBuffer =
      AggregateBuffer(bufferData.sum + data.value, bufferData.count + 1,
        (bufferData.sum + data.value) / (bufferData.count + 1))

    /**
     * Merges two aggregation buffers.
     *
     * @param bufferData1 First buffer.
     * @param bufferData2 Second buffer to merge.
     * @return Combined aggregation buffer.
     */
    def merge(bufferData1: AggregateBuffer, bufferData2: AggregateBuffer): AggregateBuffer = {
      val newSum = bufferData1.sum + bufferData2.sum
      val newCount = bufferData1.count + bufferData2.count
      AggregateBuffer(newSum, newCount, newSum / newCount)
    }

    /**
     * Completes the aggregation by returning the final computed average.
     *
     * @param finalBufferData The final state of the aggregation buffer.
     * @return The computed average value.
     */
    def finish(finalBufferData: AggregateBuffer): Double = finalBufferData.average

    /**
     * Encoder for serializing the aggregation buffer.
     */
    def bufferEncoder: Encoder[AggregateBuffer] = Encoders.product[AggregateBuffer]

    /**
     * Encoder for serializing the output value of the aggregator.
     */
    def outputEncoder: Encoder[Double] = Encoders.scalaDouble
  }
}

/**
 * Example application demonstrating the use of a complex User-Defined Aggregator in Apache Spark.
 */
object IoTExample extends App {
  val spark = SparkSession.builder().appName("IoTExample")
    .master("local[*]").getOrCreate()

  // Setting log level to reduce verbosity
  spark.sparkContext.setLogLevel("ERROR")

  // Import Spark SQL implicits to enable functionality like `.as[SensorData]` and `groupByKey`
  import spark.implicits._

  val data = Seq(
    SensorData("sensor-1", 21.0, 1111L),
    SensorData("sensor-1", 26.0, 1112L),
    SensorData("sensor-1", 24.0, 1113L),
    SensorData("sensor-1", 25.0, 1114L),
    SensorData("sensor-1", 302.0, 1115L),
    SensorData("sensor-2", 23.0, 1111L),
    SensorData("sensor-2", 27.0, 1112L),
    SensorData("sensor-2", 28.0, 1113L),
    SensorData("sensor-2", 29.0, 1114L),
    SensorData("sensor-2", 100.0, 1115L)
  ).toDF("sensorID", "value", "timestamp")

  // Showing the DataFrame to verify the content
  data.show(false)

  // Creating a TypedColumn using the custom aggregator
  val averageSensorValue: TypedColumn[SensorData, Double] =
    UdaExample2.AverageSensorValue.toColumn.name("averageSensorValue")

  // Using groupByKey requires converting DataFrame to Dataset[SensorData]
  val averageSensorValueDF = data.as[SensorData]
    .groupByKey(_.sensorID)
    .agg(averageSensorValue)
    .toDF("sensorID", "averageSensorValue")

  // Showing the resulting DataFrame with averages
  averageSensorValueDF.show()
}
