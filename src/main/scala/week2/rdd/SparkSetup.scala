package week2.rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
 * Setup and management of SparkContext and SparkSession.
 */
object SparkSetup {
  val conf: SparkConf = new SparkConf().setAppName("EjemplosSparkRDD").setMaster("local[*]")
  val sc: SparkContext = new SparkContext(conf)
  sc.setLogLevel("ERROR")
  val spark: SparkSession = SparkSession.builder().appName("EjemplosSparkRDD").master("local[*]").getOrCreate()

  def stop(): Unit = {
    sc.stop()
    spark.stop()
  }
}