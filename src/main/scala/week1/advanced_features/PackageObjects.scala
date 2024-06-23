package week1.advanced_features

/**
 * Explores the usage of package objects in Scala, which are used to hold package-wide definitions
 * that can be accessed across different files within the same package. This mechanism allows sharing
 * commonly used functions, constants, and implicit conversions in a centralized location, facilitating
 * easier maintenance and cleaner code organization.
 */
package object utils {
  val DefaultTimeout = 1000
  def calculateTimeout(retries: Int): Int = retries * DefaultTimeout
}

object PackageObjectsExample extends App {
  import utils._

  // Accessing constant and function from package object
  println(s"Default Timeout: $DefaultTimeout")
  println(s"Timeout for 5 retries: ${calculateTimeout(5)}")

  // Demonstrating practical application of shared utilities
  val operationTimeout = calculateTimeout(3)
  println(s"Operation timeout after 3 retries: $operationTimeout")
}
