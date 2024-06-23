package week1.advancedfeatures.packageobjects

/**
 * The `utils` package object in Scala serves as a centralized location to define functions, constants,
 * type aliases, and implicit conversions accessible across the `week1.advanced_features.packageobjects.utils` package.
 * This design aids in maintaining organized and DRY (Don't Repeat Yourself) code by offering commonly used utilities,
 * facilitating easier and more consistent access across the package.
 *
 * Package objects are particularly useful for simplifying access to common functionalities within a package,
 * reducing the need for repetitive imports. Members of a package object are accessible throughout the package
 * without needing explicit imports if you are within the same package scope.
 *
 * Additionally, this package object demonstrates the use of case classes within the package context,
 * which are ideal for modeling immutable data. Case classes provide a concise way to define classes and
 * are beneficial for patterns like matching, which are frequently used in functional programming.
 *
 * @example Case class for representing financial accounts:
 *          {{{
 *          // A case class to represent a financial account, typically used to model data succinctly and immutably.
 *          case class Account(id: AccountID, balance: Balance)
 *          }}}
 *          This case class includes automatic support for equality checks, copying, and has automatically generated
 *          toString methods which are useful for logging and debugging purposes within utilities managed here.
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

  type UserProfile = Map[String, String]

  val sampleProfile: UserProfile = Map("name" -> "John Doe", "email" -> "john@example.com")


  type AccountID = String
  type Balance = Double
  type AccountMap = Map[AccountID, Balance]

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
