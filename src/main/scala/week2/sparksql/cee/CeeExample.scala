package week2.sparksql.cee


import org.apache.spark.sql.catalyst.expressions.{Expression, Literal, UnaryExpression}
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.types.{DataType, StringType}
import week2.sparksql.SparkSessionWrapper

/**
 * Object containing custom Spark SQL functions for use in DataFrame operations.
 */
object ExampleFunctions {
  /**
   * A custom `UnaryExpression` that converts a string to lowercase.
   * It extends `CodegenFallback` to provide a non-code-generated implementation
   * of the expression, which is useful when code generation is not feasible.
   */
  val toLower: UnaryExpression with CodegenFallback = new UnaryExpression with CodegenFallback {

    /**
     * Returns the child expression that this unary expression operates on.
     * Here, it is a literal string "HELLO WORLD".
     */
    override def child: Expression = Literal("HELLO WORLD")

    /**
     * Evaluates the expression safely assuming the input is non-null.
     * Converts the input string to lowercase.
     *
     * @param input the input value to the expression, guaranteed not to be null.
     * @return the lowercase representation of the input string.
     */
    override protected def nullSafeEval(input: Any): Any = input.toString.toLowerCase

    /**
     * Indicates whether this instance can equal another instance.
     *
     * @param that the instance to compare to.
     * @return `true` to indicate it can be equal to any other object.
     */
    override def canEqual(that: Any): Boolean = true

    /**
     * Creates a new copy of this expression with a new child.
     *
     * @param newChild the new child expression.
     * @return a new instance of the unary expression with the updated child.
     */
    override protected def withNewChildInternal(newChild: Expression): Expression = newChild

    /**
     * Returns the data type of this expression, which is `StringType`.
     */
    override def dataType: DataType = StringType

    /**
     * Returns the arity of this product, which is 0 because it does not contain any subexpressions.
     */
    override def productArity: Int = 0

    /**
     * Throws an `IndexOutOfBoundsException` because this expression does not have product elements.
     *
     * @param n the index of the product element.
     */
    override def productElement(n: Int): Any = throw new IndexOutOfBoundsException
  }
}

import org.apache.spark.sql.functions.{col, udf}

/**
 * Example application to demonstrate the use of a custom UnaryExpression within a Spark DataFrame.
 */
object CEExample02 extends App with SparkSessionWrapper {

  // Define a user-defined function (UDF) that wraps the custom expression.
  val toLowerCase = udf[String, String](_.toLowerCase())

  // Set Spark log level to ERROR to minimize log output.
  spark.sparkContext.setLogLevel("ERROR")
  import spark.implicits._

  // Create a DataFrame containing a single string column.
  val data = Seq("HELLO WORLD").toDF("text")
  data.show(false)

  // Use the UDF to add a new column to the DataFrame where the text is converted to lowercase.
  val lowerCaseDF = data.withColumn("lowerCase", toLowerCase(col("text")))
  lowerCaseDF.show(false)
}
