package week2.encoders

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.scheduler.{SparkListener, SparkListenerTaskEnd}
import org.apache.spark.sql.SparkSession

import java.util.concurrent.atomic.AtomicLong

/**
 * Demonstrates serialization performance monitoring using Spark's Kryo serialization and capturing performance metrics.
 */
object SerializationComparisonWithMetrics {

  /**
   * Entry point for the Spark application to measure serialization performance with metrics.
   * @param args Command-line arguments (not used).
   */
  def main(args: Array[String]): Unit = {
    // Configure Spark to use Kryo as the serializer
    val conf = new SparkConf()
      .setAppName("SerializationComparisonWithMetrics")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    // Initialize a Spark session with the specified configuration
    val spark = SparkSession.builder().config(conf).master("local[2]").getOrCreate()

    // Set log level to ERROR to reduce log output during execution
    spark.sparkContext.setLogLevel("ERROR")

    // Create a list of example data
    val baseData = List("Alice", "Bob", "Charlie", "David", "Mario", "Ana", "Felipe", "Maria")
    val data = (1 to 1000000).toList.map(i => (i, baseData((i - 1) % baseData.length)))

    // Create an RDD from the list
    val rdd: RDD[(Int, String)] = spark.sparkContext.parallelize(data)

    // Attach a custom SparkListener to gather performance metrics
    spark.sparkContext.addSparkListener(new MySparkListener)

    // Perform serialization and measure the time taken using Kryo
    val startWithKryo = System.currentTimeMillis()
    rdd.map { case (id, name) => (id, name.length) }.collect()
    val endWithKryo = System.currentTimeMillis()

    // Stop the Spark session
    spark.stop()

    // Print the time taken for serialization
    val timeWithKryo = endWithKryo - startWithKryo
    println(s"Time with Kryo: $timeWithKryo ms")

    // Print memory and CPU metrics gathered by the SparkListener
    println(s"Memory used: ${MySparkListener.memoryUsed.get()} MB")
    println(s"CPU used: ${MySparkListener.cpuUsed.get()} %")
  }
}

/**
 * A custom SparkListener that updates metrics on task completion.
 */
class MySparkListener extends SparkListener {
  override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    // Update memory and CPU usage metrics after each task ends
    MySparkListener.memoryUsed.set(taskEnd.taskMetrics.memoryBytesSpilled / (1024 * 1024)) // Convert to MB
    MySparkListener.cpuUsed.set(taskEnd.taskMetrics.executorCpuTime / 1000000) // Convert to percentage
  }
}

/**
 * Object to hold and manage performance metrics as atomic longs.
 */
object MySparkListener {
  val memoryUsed: AtomicLong = new AtomicLong(0)
  val cpuUsed: AtomicLong = new AtomicLong(0)
}
