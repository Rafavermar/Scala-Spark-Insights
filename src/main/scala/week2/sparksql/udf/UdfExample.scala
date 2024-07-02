package week2.sparksql.udf

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{explode, udf}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * A trait to manage a singleton instance of SparkSession for the application.
 */
trait SparkSessionWrapper {
  /**
   * Creates a lazy SparkSession that initializes only once when it is first accessed.
   * The session is configured for local execution and to suppress verbose logging.
   */
  implicit lazy val spark: SparkSession = {
    val conf = new org.apache.spark.SparkConf().setAppName("UDFExample01")
      .setMaster("local[*]")

    val sparkSession = SparkSession.builder().config(conf).getOrCreate()
    sparkSession.sparkContext.setLogLevel("ERROR")
    sparkSession
  }
}

/**
 * Object containing functions for text processing using UDFs in Spark.
 */
object UdfExample extends SparkSessionWrapper {
  // Regular expressions for text manipulation.
  val pattern: String = """[\p{Punct}]"""
  val splitPattern: String = "\\s+"

  /**
   * Removes punctuation from the text and converts it to lower case.
   * @param text the input string from which punctuation will be removed.
   * @return a string without punctuation.
   */
  def removePunctuation(text: String): String = text.toLowerCase.replaceAll(pattern, "")

  /**
   * Splits the given text into words based on spaces.
   * @param text the input string to be split.
   * @return an array of words.
   */
  def splitBySpaces(text: String): Array[String] = text.split(splitPattern)

  /**
   * Groups words in an array and counts the occurrences of each word.
   * @param words an array of words.
   * @return a map with words as keys and their counts as values.
   */
  def groupByWords(words: Array[String]): Map[String, Int] = {
    words.groupBy(identity).mapValues(_.length).toMap // Convert MapView to Map
  }

  /**
   * User Defined Function (UDF) that combines text processing functions to count words.
   */
  val wordsCount: UserDefinedFunction = udf { (textContent: String) =>
    val textWithoutPunctuation = removePunctuation(textContent)
    val words = splitBySpaces(textWithoutPunctuation)
    groupByWords(words)
  }

  /**
   * Creates a DataFrame from a sequence of tuples representing ID and text data.
   * @param data a sequence of tuples containing ID and text.
   * @return a DataFrame with two columns: "id" and "text".
   */
  def createDataFrame(data: Seq[(String, String)]): DataFrame = {
    import spark.implicits._
    data.toDF("id", "text")
  }

  /**
   * Applies the wordsCount UDF to a DataFrame to add a new column "wordCount".
   * @param df the input DataFrame containing a "text" column.
   * @return a DataFrame with an additional "wordCount" column.
   */
  def applyWordCount(df: DataFrame): DataFrame = {
    import spark.implicits._
    df.withColumn("wordCount", wordsCount($"text"))
  }

  /**
   * Explodes the "wordCount" map column into two columns "key" and "value" and aggregates the results.
   * @param df the DataFrame with the "wordCount" map column.
   * @return a DataFrame grouped by "key" with the sum of "value".
   */
  def explodeWordCount(df: DataFrame): DataFrame = {
    import spark.implicits._
    df.select($"id", explode($"wordCount"))
      .groupBy("key").sum("value")
  }
}

/**
 * Main application for demonstrating the usage of UDFs in Spark to process text data.
 */
object UDFExample01 extends App with SparkSessionWrapper {
  import UdfExample._
  // Creating the DataFrame
  val data = Seq(("1", "Hello world"), ("2", "I love Spark"), ("3", "I used to love Hi Spark world"))
  val df = createDataFrame(data)

  // Applying the UDF to count words
  val wordCountDF = applyWordCount(df)
  wordCountDF.show(false)

  // Expanding the map to have a column for each word
  val explodedWCDF = explodeWordCount(wordCountDF)
  explodedWCDF.show()
}
