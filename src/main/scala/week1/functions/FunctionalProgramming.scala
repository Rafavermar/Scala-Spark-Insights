package week1.functions

/**
 * Demonstrates various advanced Scala function concepts including lambda expressions,
 * higher-order functions, currying, and function composition. This object serves as a foundation
 * for practical examples and is utilized in the worksheet `FunctionExamples.sc` for direct,
 * interactive experimentation. Additionally, it supports the `ValidationsBenchmark` class
 * which benchmarks the performance of certain functions defined here.
 *
 * Note on Initialization:
 * Fields such as `sumLambda` and `addFive` are declared with `lazy` to prevent `NullPointerException`.
 * Using `lazy` ensures these fields are initialized only when they are first accessed, which is crucial
 * because Scala's `App` trait defers the initialization of fields until after the main method begins execution.
 * This lazy approach avoids issues where fields might be accessed before they are initialized.
 */

object FunctionalProgramming extends App {

  // Lambda functions (anonymous functions)
  /**
   * Defines a lambda function for summing two integers. This can be instantiated in three ways:
   * 1. As a value with explicitly named parameters: `(Int, Int) => Int = (a, b) => a + b` for clarity.
   * 2. As a method that returns a function: `def sumLambda: (Int, Int) => Int = _ + _` to provide fresh instances on each call, useful in multi-threaded contexts or when capturing mutable state.
   * 3. As a value using placeholder syntax: `val sumLambda: (Int, Int) => Int = _ + _` for conciseness, suitable for simple operations where parameter clarity is less critical.
   * Each approach offers different benefits regarding readability, memory efficiency, and instance management, impacting performance and usage in various contexts.
   */

  println("Demo: Lambda Function")
  lazy val sumLambda: (Int, Int) => Int = (a, b) => a + b
  val result1 = sumLambda(2, 3)
  println(s"Result of sumLambda: $result1\n")


  println("Demo: Higher-Order Function")
  def performOperation(a: Int, b: Int, operation: (Int, Int) => Int): Int = operation(a, b)
  val result2 = performOperation(2, 3, sumLambda)
  println(s"Result of performOperation: $result2\n")


  println("Demo: Currying")
  def multiplyBy(factor: Int): Int => Int = (a: Int) => a * factor
  val triple = multiplyBy(3)
  val result3 = triple(3)
  println(s"Result of triple(3): $result3\n")


  println("Demo: Partially Applied Function")
  def sum(a: Int)(b: Int): Int = a + b
  lazy val addFive = sum(5)_
  val result4 = addFive(3)
  println(s"Result of addFive(3): $result4\n")


  println("Demo: Function Composition")
  def multiplyList(nums: List[Int])(factor: Int): List[Int] = nums.map(_ * factor)
  val doubledList = multiplyList(List(1, 2, 3))(2)
  println(s"Doubled list: $doubledList\n")

  /**
   * Sums two lists element-wise. Utilizes the `zip` method to combine elements from two lists into tuples,
   * then maps over these tuples to sum each paired elements.
   *
   * The `zip` method pairs elements from the two lists into a list of tuples. This is useful when you need to
   * perform operations on corresponding elements from two sequences.
   *
   * @param list1 The first list of integers.
   * @param list2 The second list of integers.
   * @return A list containing the sum of corresponding elements from list1 and list2.
   */

  println("Demo: Lists Sum")
  def sumLists(list1: List[Int], list2: List[Int]): List[Int] = {
    list1.zip(list2).map { case (a, b) => a + b }
  }
  val listOne = List(1, 2, 3)
  val listTwo = List(4, 5, 6)
  val summedLists = sumLists(listOne, listTwo)
  println(s"Summed lists: $summedLists") // Output: Summed lists: List(5, 7, 9)

}
