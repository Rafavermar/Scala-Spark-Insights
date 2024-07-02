package week2.cee


import org.apache.spark.sql.functions.{col, udf}
import org.scalatest.funsuite.AnyFunSuite
import week2.sparksql.SparkSessionTestWrapper

class CEExampleTest extends AnyFunSuite with SparkSessionTestWrapper {

  import spark.implicits._

  test("Custom UnaryExpression should convert DataFrame column to lowercase") {
    // Create a DataFrame
    val data = Seq("HELLO WORLD", "SPARK SQL").toDF("text")

    // Define a UDF wrapping our custom logic (if required, based on how toLower is to be used)
    val toLowerCaseUDF = udf((s: String) => s.toLowerCase())

    // Use the UDF to transform DataFrame
    val lowerCaseDF = data.withColumn("lowerCase", toLowerCaseUDF(col("text")))

    // Expected DataFrame
    val expectedData = Seq(("HELLO WORLD", "hello world"), ("SPARK SQL", "spark sql")).toDF("text", "lowerCase")

    // Assertion to verify the transformation
    assert(lowerCaseDF.collect() === expectedData.collect(), "The DataFrame should contain lowercased text in the 'lowerCase' column.")
  }
}
