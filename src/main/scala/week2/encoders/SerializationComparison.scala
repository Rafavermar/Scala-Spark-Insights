package week2.encoders

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * A program to compare the performance of Kryo serialization versus Java serialization in Apache Spark.
 */
object SerializationComparison {

  /**
   * Main function to execute the serialization comparison.
   * @param args Command-line arguments (not used in this program).
   */
  def main(args: Array[String]): Unit = {
    // Configure Spark to use Kryo as the serializer
    val conf = new SparkConf()
      .setAppName("SerializationComparison")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    // Create a Spark session configured for local execution and Kryo serialization
    val spark = SparkSession.builder().config(conf).master("local[2]").getOrCreate()

    // Create a list of sample data
    val data = List((1, "Alice"), (2, "Bob"), (3, "Charlie"), (4, "David"))

    // Create an RDD from the list
    val rdd: RDD[(Int, String)] = spark.sparkContext.parallelize(data)

    // Perform serialization and measure time with Kryo
    val startWithKryo = System.currentTimeMillis()
    rdd.map { case (id, name) => (id, name.length) }.collect()
    val endWithKryo = System.currentTimeMillis()

    // Configure Spark to use Java serialization
    conf.set("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
    val sparkWithoutKryo = SparkSession.builder().config(conf).getOrCreate()

    // Create an RDD using Java serialization
    val rddWithoutKryo: RDD[(Int, String)] = sparkWithoutKryo.sparkContext.parallelize(data)

    // Perform serialization and measure time without Kryo
    val startWithoutKryo = System.currentTimeMillis()
    rddWithoutKryo.map { case (id, name) => (id, name.length) }.collect()
    val endWithoutKryo = System.currentTimeMillis()

    // Calculate and print the execution time with and without Kryo
    val timeWithKryo = endWithKryo - startWithKryo
    val timeWithoutKryo = endWithoutKryo - startWithoutKryo
    println(s"Time with Kryo: $timeWithKryo ms")
    println(s"Time without Kryo: $timeWithoutKryo ms")

    // Stop the Spark sessions
    spark.stop()
    sparkWithoutKryo.stop()
  }
}
