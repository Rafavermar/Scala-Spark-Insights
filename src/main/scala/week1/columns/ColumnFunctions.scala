package week1.columns


import org.apache.spark.sql.{Column, SparkSession}
import org.apache.spark.sql.functions._

import scala.Console.{BLUE, BOLD, GREEN, RESET}

/**
 * Defines a set of functions to manipulate string columns in Spark DataFrames.
 */
object ColumnFunctions {

  /**
   * Converts all characters in the column to lowercase and removes any whitespace.
   *
   * @param col The column to be transformed.
   * @return A new Column with all characters in lowercase and no whitespace.
   */
  def lowercaseWithoutWhitespace(col: Column): Column = {
    lower(regexp_replace(col, "\\s+", ""))
  }

  /**
   * Converts all characters in the column to uppercase and removes any whitespace.
   *
   * @param col The column to be transformed.
   * @return A new Column with all characters in uppercase and no whitespace.
   */
  def uppercaseWithoutWhitespace(col: Column): Column = {
    upper(regexp_replace(col, "\\s+", ""))
  }

  /**
   * Removes all special characters from the column, retaining only alphanumeric characters.
   *
   * @param col The column to be transformed.
   * @return A new Column with only alphanumeric characters.
   */
  def removeSpecialCharacters(col: Column): Column = {
    regexp_replace(col, "[^a-zA-Z0-9]", "")
  }

  /**
   * Converts the column to lowercase, removes all special characters and whitespace.
   *
   * @param col The column to be transformed.
   * @return A new Column processed as described.
   */
  def removeSpecialCharactersAndWhitespaceInLower(col: Column): Column = {
    lowercaseWithoutWhitespace(removeSpecialCharacters(col))
  }

  /**
   * Converts the column to uppercase, removes all special characters and whitespace.
   *
   * @param col The column to be transformed.
   * @return A new Column processed as described.
   */
  def removeSpecialCharactersAndWhitespaceInUpper(col: Column): Column = {
    uppercaseWithoutWhitespace(removeSpecialCharacters(col))
  }
}

/**
 * Application to demonstrate the usage of defined column functions on Spark DataFrames.
 */
object ColumnFunctionsApp extends App {

  val spark = SparkSession.builder()
    .appName("ColumnFunctionsApp")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  val df = Seq(
    ("Messi", 100, 50, "America", "Argentina"),
    (" Ronaldo!!", 90, 40, "Europe", "Portugal"),
    ("Neymar", 80, 30, "America", "Brazil"),
    ("Mbappe_ ", 70, 20, "Europe", "France"),
  ).toDF("name", "goals", "assists", "continent", "country")

  df.show()

  val resDF = df
    .withColumn("name_no_whitespace", ColumnFunctions.lowercaseWithoutWhitespace(col("name")))
    .withColumn("name_no_special_characters", ColumnFunctions.removeSpecialCharacters(col("name_no_whitespace")))

  resDF.show()

  val resDF2 = df
    .withColumn("nombre_normalizado",
      ColumnFunctions.lowercaseWithoutWhitespace(
        ColumnFunctions.removeSpecialCharacters(
          ColumnFunctions.uppercaseWithoutWhitespace(col("name")))
      )
    )

  resDF2.show(truncate = false)

  println("Explanation of DataFrame execution plans:")
  println(BOLD + BLUE + "resDF: Without function concatenation" + RESET)
  resDF.explain(extended = true)
  println()
  println(BOLD + GREEN + "resDF2: With function concatenation" + RESET)
  resDF2.explain(extended = true)

  spark.stop()
}
