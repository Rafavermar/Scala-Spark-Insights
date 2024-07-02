package week2.sparksql.udf

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import week2.sparksql.SparkSessionTestWrapper

class UdfExampleTest extends AnyFunSuite with Matchers with SparkSessionTestWrapper {
  import UdfExample._

  test("removePunctuation should remove all punctuation and convert to lower case") {
    val input = "Hello, World!"
    val expected = "hello world"
    assert(removePunctuation(input) === expected)
  }

  test("splitBySpaces should correctly split text into words") {
    val input = "hello world"
    val expected = Array("hello", "world")
    assert(splitBySpaces(input) === expected)
  }

  test("groupByWords should count occurrences of each word") {
    val input = Array("hello", "world", "hello")
    val expected = Map("hello" -> 2, "world" -> 1)
    assert(groupByWords(input) === expected)
  }

  test("explodeWordCount should expand word counts into individual rows and sum them") {
    val df = createDataFrame(Seq(("1", "Hello, world! Hello world.")))
    val wordCountDF = applyWordCount(df)
    val explodedDF = explodeWordCount(wordCountDF)
    val collectedResults = explodedDF.collect()

    // Assuming explode and groupBy operations are done correctly in explodeWordCount
    assert(collectedResults.length === 2) // For "hello" and "world"
  }
}
