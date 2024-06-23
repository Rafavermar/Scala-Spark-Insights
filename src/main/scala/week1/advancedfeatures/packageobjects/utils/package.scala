package week1.advancedfeatures.packageobjects

/**
 * The `utils` package object in Scala serves as a centralized place to define functions, constants,
 * type aliases, and implicit conversions that are meant to be accessible across all classes and
 * objects within the `week1.advanced_features.packageobjects.utils` package. This approach helps in
 * maintaining clean and organized code and avoids duplication across the package.
 */
package object utils {
  /**
   * Logs a message to the console, demonstrating how package objects can contain utility functions
   * that are widely useful across multiple classes in the package.
   * @param message The message to be logged.
   */
  def log(message: String): Unit = println(s"Log: $message")

  /**
   * Default timeout used across various functions that require a timeout parameter.
   * This demonstrates the use of package objects to store widely used constants.
   */
  val DefaultTimeout: Int = 5000

  /**
   * Calculates the square of a number. This method exemplifies how mathematical operations
   * or frequently used computations can be centralized in a package object.
   * @param x The number to square.
   * @return The square of the number.
   */
  def square(x: Double): Double = x * x

  /**
   * A type alias for a 2D matrix of doubles, showing how package objects can be used
   * to simplify complex type notations and enhance code readability.
   */
  type Matrix = List[List[Double]]
}