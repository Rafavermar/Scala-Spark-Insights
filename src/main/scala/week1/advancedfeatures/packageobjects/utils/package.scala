package week1.advancedfeatures.packageobjects

/**
 * The `utils` package object in Scala is a centralized location to define functions, constants,
 * type aliases, and implicit conversions accessible across the `week1.advanced_features.package objects.utils` package.
 * It helps maintain organized and DRY (Don't Repeat Yourself) code by providing commonly used utilities.
 * Package objects are designed to hold functions, constants, type aliases, and implicit conversions that should be accessible across all classes
 * and objects within the same package.
 * They are typically not meant to replace the import statements but to simplify the access to common functionalities within a package.
 * The members of a package object are accessible in the package without needing to import them explicitly if you are within the same package.
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

  /** Adds a function to calculate the determinant of a 2x2 matrix, enhancing the mathematical capabilities of the package.
   *
   * @param matrix
   * @return
   */
  def determinant(matrix: Matrix): Double = {
    if (matrix.length == 2 && matrix.head.length == 2) {
      matrix(0)(0) * matrix(1)(1) - matrix(0)(1) * matrix(1)(0)
    } else {
      throw new IllegalArgumentException("Matrix must be 2x2.")
    }
  }

  // A type alias for a user profile in a simplified form, showing how to manage complex data structures.
  type UserProfile = Map[String, String]

  // Constant for a sample user profile, demonstrating the use of type aliases for complex data.
  val sampleProfile: UserProfile = Map("name" -> "John Doe", "email" -> "john@example.com")


  // New type aliases for financial operations
  type AccountID = String
  type Balance = Double
  type AccountMap = Map[AccountID, Balance]

  // Case class to represent a financial account
  case class Account(id: AccountID, balance: Balance)

  /** Function to apply interest to all accounts in a map
   *
   * @param accounts
   * @param rate
   * @return
   */
  def applyInterest(accounts: AccountMap, rate: Double): AccountMap = {
    accounts.map { case (id, balance) => (id, balance * (1 + rate)) }
  }
}
