package week2.sparksql.uda


import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, TypedColumn}
import org.apache.spark.sql.expressions.Aggregator

/**
 * A case class representing a value with its associated count.
 * @param value The numeric value.
 * @param count The count of occurrences for the value.
 */
final case class ValueWithCount(value: Double, count: Int)

/**
 * Defines an aggregator to calculate a percentile distribution.
 */
object UdaExample1 {
  /**
   * Aggregator to compute percentiles from a dataset of ValueWithCount.
   */
  object Percentile extends Aggregator[ValueWithCount, Map[Int, Double], Map[Int, Double]] {
    /**
     * The initial zero value for the aggregation.
     * @return An empty Map of Int to Double.
     */
    def zero: Map[Int, Double] = Map.empty[Int, Double]

    /**
     * Reduces the input data into the aggregation buffer.
     * @param buffer The aggregation buffer, a map of integer values to their respective double counts.
     * @param valueCount The input data of ValueWithCount.
     * @return Updated buffer with the new value added.
     */
    def reduce(
                buffer: Map[Int, Double],
                valueCount: ValueWithCount,
              ): Map[Int, Double] = buffer +
      (valueCount.value.toInt -> valueCount.count.toDouble)

    /**
     * Merges two aggregation buffers.
     * @param b1 The first buffer.
     * @param b2 The second buffer.
     * @return A merged map containing the sum of counts for each value.
     */
    def merge(b1: Map[Int, Double], b2: Map[Int, Double]): Map[Int, Double] =
      mergeMaps(b1, b2)

    /**
     * Completes the aggregation by normalizing the counts to percentages.
     * @param reduction The final aggregation map.
     * @return A map where each count is normalized to represent a percentage.
     */
    def finish(reduction: Map[Int, Double]): Map[Int, Double] =
      normalizeMap(reduction)

    /**
     * Specifies the encoder for the intermediate data type, Map[Int, Double].
     * @return An encoder for serializing Map[Int, Double].
     */
    def bufferEncoder: Encoder[Map[Int, Double]] = Encoders.kryo[Map[Int, Double]]

    /**
     * Specifies the encoder for the output data type, Map[Int, Double].
     * @return An encoder for serializing Map[Int, Double].
     */
    def outputEncoder: Encoder[Map[Int, Double]] = Encoders.kryo[Map[Int, Double]]

    /**
     * Merges two maps by summing the values of identical keys.
     * @param map1 The first map.
     * @param map2 The second map.
     * @return A merged map.
     */
    private def mergeMaps(
                           map1: Map[Int, Double],
                           map2: Map[Int, Double],
                         ): Map[Int, Double] = (map1.toSeq ++ map2.toSeq).groupBy(_._1).view
      .mapValues(_.map(_._2).sum).toMap

    /**
     * Normalizes a map of counts to their relative percentages.
     * @param map A map of integer values to their respective double counts.
     * @return A map where each count is divided by the total of all counts.
     */
    private def normalizeMap(map: Map[Int, Double]): Map[Int, Double] = {
      val totalValueCount = map.values.sum
      map.map { case (value, count) => value -> (count / totalValueCount) }
    }
  }
}

/**
 * Application to demonstrate the usage of a complex user-defined aggregator.
 */
object UDAExample01 extends App {
  val spark = SparkSession.builder().appName("ComplexUDAExample")
    .master("local[*]").getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")
  import spark.implicits._

  val data = Seq((1.0, 2), (2.0, 3), (3.0, 4)).toDF("value", "count")
  data.as[ValueWithCount].show(false)

  val percentiles: TypedColumn[ValueWithCount, Map[Int, Double]] =
    UdaExample1.Percentile.toColumn.name("percentiles")
  val percentilesDF: DataFrame = data.as[ValueWithCount].groupByKey(_ => true)
    .agg(percentiles).select("percentiles")
  percentilesDF.show(truncate = false)
}

