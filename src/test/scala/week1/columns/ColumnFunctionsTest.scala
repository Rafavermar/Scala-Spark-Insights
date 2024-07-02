package week1.columns

import org.apache.spark.sql.functions.col
import org.scalatest.funsuite.AnyFunSuite
import week2.sparksql.SparkSessionTestWrapper

class ColumnFunctionsTest extends AnyFunSuite with SparkSessionTestWrapper {

  import spark.implicits._

  test("lowercaseWithoutWhitespace should convert column values to lowercase without whitespace") {
    val df = Seq(" Test String ").toDF("column")
    val result = df.select(ColumnFunctions.lowercaseWithoutWhitespace(col("column"))).as[String].collect()
    assert(result === Array("teststring"))
  }

  test("uppercaseWithoutWhitespace should convert column values to uppercase without whitespace") {
    val df = Seq(" Test String ").toDF("column")
    val result = df.select(ColumnFunctions.uppercaseWithoutWhitespace(col("column"))).as[String].collect()
    assert(result === Array("TESTSTRING"))
  }

  test("removeSpecialCharacters should remove non-alphanumeric characters") {
    val df = Seq("abc#123!").toDF("column")
    val result = df.select(ColumnFunctions.removeSpecialCharacters(col("column"))).as[String].collect()
    assert(result === Array("abc123"))
  }

  test("removeSpecialCharactersAndWhitespaceInLower should process the column to lowercase, remove special characters and whitespace") {
    val df = Seq(" ABC DEF#123! ").toDF("column")
    val result = df.select(ColumnFunctions.removeSpecialCharactersAndWhitespaceInLower(col("column"))).as[String].collect()
    assert(result === Array("abcdef123"))
  }

  test("removeSpecialCharactersAndWhitespaceInUpper should process the column to uppercase, remove special characters and whitespace") {
    val df = Seq(" abc def#123! ").toDF("column")
    val result = df.select(ColumnFunctions.removeSpecialCharactersAndWhitespaceInUpper(col("column"))).as[String].collect()
    assert(result === Array("ABCDEF123"))
  }
}
