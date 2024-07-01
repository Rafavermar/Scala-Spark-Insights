package week2.rdd

import SparkSetup.sc

/**
 * The `CustomFunctions` object demonstrates the implementation and application of complex
 * custom functions within Spark RDD transformations. It illustrates how to integrate
 * bespoke logic into Spark's distributed computing framework to manipulate and evaluate data
 * based on user-defined conditions and mathematical operations.
 *
 * Overview:
 * - **Custom Function Definitions**: Includes definitions for several custom functions that
 *   perform various mathematical operations. These functions are designed to showcase how
 *   complex logic can be encapsulated within functions to be applied across distributed datasets.
 *
 * - **Complex Conditional Logic**: Features `funcionCompleja`, a function that applies different
 *   mathematical operations based on the value of its input. This function demonstrates conditional
 *   logic within RDD transformations, highlighting Spark's capability to handle non-linear and
 *   multi-branch computations within its transformations.
 *
 * - **RDD Transformation**: Utilizes these custom functions to transform an RDD of integers. This
 *   transformation is conducted via the `map` operation, applying `funcionCompleja` to each element
 *   in the RDD, which in turn may apply one of several custom functions based on the element's value.
 */
object CustomFunctions extends App {
  def unaFuncionCustomizada(x: Int): Int = x * x + 2 * x + 1
  def unaFuncionCustomizada2(x: Int): Int = x * x * x
  def funcionCompleja(x: Int): Int = {
    if (x % 2 == 0) x * x
    else if (x % 3 == 0) unaFuncionCustomizada(x * x * x)
    else unaFuncionCustomizada2(x * x + 2 * x + 1)
  }

  val rdd2 = sc.parallelize(Seq(1, 2, 3, 4, 5))
  val rddTransformado = rdd2.map(funcionCompleja)
  println("--->>> RDD Transformado usando la función compleja: ")
  rddTransformado.collect().foreach(println)

  SparkSetup.stop()
}