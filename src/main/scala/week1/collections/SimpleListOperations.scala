package week1.collections

/**
 *  basic operations on Scala lists, such as adding elements,
 * reversing the list, and filtering for specific elements.
 */
object SimpleListOperations extends App {
  /**
   * Adds an element to the list and returns a new list.
   */
  def addElement(list: List[Int], element: Int): List[Int] = list :+ element

  /**
   * Reverses the elements of the list.
   */
  def reverseList(list: List[Int]): List[Int] = list.reverse

  /**
   * Filters the list to only include even numbers.
   */
  def filterEvens(list: List[Int]): List[Int] = list.filter(_ % 2 == 0)

  /**
   * Uses `map` to transform list elements.
   */
  def doubleElements(list: List[Int]): List[Int] = list.map(_ * 2)

  /**
   * Uses `flatMap` to flatten a list of lists.
   */
  def flattenList(lists: List[List[Int]]): List[Int] = lists.flatMap(identity)

  /**
   * Applies a function to each element using `foreach`.
   */
  def printElements(list: List[Int]): Unit = list.foreach(println)

  /**
   * Uses `reduce` to combine elements using a binary operation.
   */
  def sumElements(list: List[Int]): Int = list.reduce(_ + _)

  /**
   * Uses `fold` to carry over a temporary result through all elements.
   */
  def computeRunningTotal(list: List[Int], initial: Int): Int = list.foldLeft(initial)(_ + _)

  /**
   * Groups elements based on a predicate using `groupBy`.
   */
  def groupByModulo(list: List[Int]): Map[Int, List[Int]] = list.groupBy(_ % 2)

  /**
   * Partitions the list into two based on a predicate.
   */
  def partitionEvensAndOdds(list: List[Int]): (List[Int], List[Int]) = list.partition(_ % 2 == 0)

  /**
   * Combines elements with their indices using `zipWithIndex`.
   */
  def zipWithIndex(list: List[Int]): List[(Int, Int)] = list.zipWithIndex

  /**
   * Splits the list at a specified position using `splitAt`.
   */
  def splitAtPosition(list: List[Int], position: Int): (List[Int], List[Int]) = list.splitAt(position)

  /**
   * Uses `span` to split the list based on the first match failure.
   */
  def spanBelowThreshold(list: List[Int], threshold: Int): (List[Int], List[Int]) = list.span(_ < threshold)

  /**
   * Uses `dropWhile` to skip elements until a predicate fails.
   */
  def dropWhileBelowThreshold(list: List[Int], threshold: Int): List[Int] = list.dropWhile(_ < threshold)

  /**
   * Uses `find` to locate the first element matching a condition.
   */
  def findFirstAboveThreshold(list: List[Int], threshold: Int): Option[Int] = list.find(_ > threshold)

  /**
   * Checks if any element meets a condition using `exists`.
   */
  def checkIfExistsAboveThreshold(list: List[Int], threshold: Int): Boolean = list.exists(_ > threshold)

  // Test data
  val numbers = List(1, 2, 3, 4, 5, 6)
  val numbersLists = List(List(1, 2), List(3, 4), List(5, 6))

  // Function calls
  println("Added Element: " + addElement(numbers, 7))
  println("Reversed List: " + reverseList(numbers))
  println("Filtered Evens: " + filterEvens(numbers))
  println("Doubled Elements: " + doubleElements(numbers))
  println("Flattened List: " + flattenList(numbersLists))
  print("Printed Elements: ")
  printElements(numbers)
  println("Sum of Elements: " + sumElements(numbers))
  println("Running Total: " + computeRunningTotal(numbers, 0))
  println("Grouped by Modulo: " + groupByModulo(numbers))
  println("Partitioned Evens and Odds: " + partitionEvensAndOdds(numbers))
  println("Zipped with Index: " + zipWithIndex(numbers))
  println("Split at Position 3: " + splitAtPosition(numbers, 3))
  println("Span Below Threshold 4: " + spanBelowThreshold(numbers, 4))
  println("Drop While Below Threshold 3: " + dropWhileBelowThreshold(numbers, 3))
  println("First Above Threshold 4: " + findFirstAboveThreshold(numbers, 4))
  println("Exists Above Threshold 6: " + checkIfExistsAboveThreshold(numbers, 6))
}
