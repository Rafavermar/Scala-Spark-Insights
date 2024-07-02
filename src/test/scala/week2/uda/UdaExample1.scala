package week2.sparksql.uda

import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, TypedColumn}
import org.apache.spark.sql.expressions.Aggregator
import org.scalatest.funsuite.AnyFunSuite
import week2.sparksql.SparkSessionTestWrapper

// Definition of case class
final case class ValueWithCount(value: Double, count: Int)

// Definition of Aggregator object
object UdaExample1 {
  object Percentile extends Aggregator[ValueWithCount, Map[Int, Double], Map[Int, Double]] {
    def zero: Map[Int, Double] = Map.empty[Int, Double]

    def reduce(buffer: Map[Int, Double], valueCount: ValueWithCount): Map[Int, Double] =
      buffer + (valueCount.value.toInt -> (buffer.getOrElse(valueCount.value.toInt, 0.0) + valueCount.count.toDouble))

    def merge(b1: Map[Int, Double], b2: Map[Int, Double]): Map[Int, Double] =
      (b1.toSeq ++ b2.toSeq).groupBy(_._1).view.mapValues(_.map(_._2).sum).toMap

    def finish(reduction: Map[Int, Double]): Map[Int, Double] = {
      val totalValueCount = reduction.values.sum
      reduction.map { case (value, count) => value -> (count / totalValueCount) }
    }

    def bufferEncoder: Encoder[Map[Int, Double]] = Encoders.map[Int, Double]

    def outputEncoder: Encoder[Map[Int, Double]] = Encoders.map[Int, Double]
  }
}

// Example application
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

// Unit test
class UdaExample1Test extends AnyFunSuite with SparkSessionTestWrapper {
  import spark.implicits._

  test("Percentile Aggregator should calculate correct percentiles") {
    val data = Seq(ValueWithCount(1.0, 2), ValueWithCount(2.0, 3), ValueWithCount(3.0, 4)).toDS()

    val percentiles: TypedColumn[ValueWithCount, Map[Int, Double]] =
      UdaExample1.Percentile.toColumn.name("percentiles")
    val result: DataFrame = data.groupByKey(_ => true).agg(percentiles).select("percentiles")

    val expected = Map(1 -> 0.2222222222222222, 2 -> 0.3333333333333333, 3 -> 0.4444444444444444)
    val actual = result.as[Map[Int, Double]].collect().head

    assert(actual == expected)
  }
}
