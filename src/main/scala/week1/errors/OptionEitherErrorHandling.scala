package week1.errors

/**
 * Provides examples of using Option and Either for error management in Scala applications.
 *
 * Option is used for safe handling of operations that might not return a value, thereby avoiding
 * the use of nulls. Either extends this by allowing an operation to return information about why
 * an operation failed, using a descriptive error message.
 *
 * Interaction Context:
 * - This class functions as an isolated module for educational purposes on error handling.
 * - It could be integrated with logging systems or error frameworks to enhance error reporting
 *   in a comprehensive software system.
 */
object OptionEitherErrorHandling extends App {
  def safeDivide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)

  println("Option Example:")
  val resultOption = safeDivide(10, 0)
  println(s"Safe division result (10 / 0): $resultOption")

  def descriptiveDivide(a: Int, b: Int): Either[String, Int] =
    if (b == 0) Left("Cannot divide by zero") else Right(a / b)

  println("Either Example:")
  val resultEither = descriptiveDivide(10, 0)
  println(s"Descriptive division result (10 / 0): $resultEither")
}
