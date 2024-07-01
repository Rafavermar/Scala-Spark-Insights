package week2.rdd

import org.apache.spark.rdd.RDD
import SparkSetup.sc
/**
 * The `RDDOperations` object demonstrates the use of Resilient Distributed Datasets (RDDs)
 * for processing and analyzing both numeric and textual data within a Spark application.
 * It showcases fundamental transformations and actions applied to RDDs, using both static
 * and dynamic input data to perform computations such as filtering and word counting.
 *
 * Functionalities Demonstrated:
 * - **Filtering RDDs**: Creates an RDD of integers and filters out even numbers, illustrating
 *   basic data filtering operations.
 *
 * - **Dynamic Text Processing**: Accepts a phrase or dynamically specified string, which is then
 *   split into words. This demonstrates the use of `flatMap` to flatten the sequence of words from
 *   each input string, followed by transformations to count the occurrences of each word.
 *
 * - **Word Count Example**: Implements the classic "Word Count" example where words from an RDD of
 *   strings are mapped to key-value pairs (word, 1) and reduced by key to count each word's occurrences.
 *   This example not only serves as a fundamental exercise in big data processing but also shows how
 *   to handle type inference issues in Scala by converting `Array` to `Seq`.
 *
 * Implementation Details:
 * - Initializes a Spark context to manage application resources and configures it for local execution.
 * - Demonstrates handling type mismatches and Scala's type inference capabilities through explicit typing
 *   and conversion techniques (e.g., converting `Array` to `Seq`), ensuring seamless integration of transformations.
 * - Utilizes `println` statements to display intermediate and final results, aiding in debugging and
 *   providing clear visibility into the data transformation process.

 */
object RDDOperations extends App {

  // Filtering RDD to find even numbers
  val rdd: RDD[Int] = sc.parallelize(Seq(1, 2, 3, 4, 5))
  val filtrarSiPar: Int => Boolean = _ % 2 == 0
  println("Filtered Even Numbers:")
  rdd.filter(filtrarSiPar).collect().foreach(println)

  // Processing a specific phrase or dynamically specified string
  val inputString = "hola mundo de Scala ..."
  val palabrasDesdeFrases: String => Seq[String] = _.split(" ").toSeq
  println("Words from input phrase:")
  palabrasDesdeFrases(inputString).foreach(println)

  // Word Count from a static RDD of strings
  val rddStrings = sc.parallelize(Seq("hola mundo", "hola", "mundo", "hola", "hola"))
  val rddWords = rddStrings.flatMap(palabrasDesdeFrases)
  println("Flattened Words from RDD:")
  rddWords.collect().foreach(println)

  // Mapping words to tuples and reducing by key for word count
  val tuplaPalabra: String => (String, Int) = palabra => (palabra, 1)
  val rddWordCount = rddWords.map(tuplaPalabra).reduceByKey(_ + _)
  println("Word Count:")
  rddWordCount.collect().foreach(println)

  SparkSetup.stop()
}