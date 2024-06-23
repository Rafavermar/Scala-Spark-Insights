package week1.validations

/**
 * Provides various functions for email validation. These functions are utilized
 * across different parts of the project to ensure email format correctness and are
 * benchmarked for performance in `ValidationsBenchmark`.
 */
object Validations {

  /** Validates if an email contains "@" symbol. */
  def containsAt(email: String): Boolean = email.contains("@")

  /** Validates if an email ends with ".com". */
  def endsWithCom(email: String): Boolean = email.endsWith(".com")

  /** Validates if an email contains a period ".". */
  def containsDot(email: String): Boolean = email.contains(".")

  /** Validates if an email does not contain spaces. */
  def containsNoSpaces(email: String): Boolean = !email.contains(" ")

  /** Validates an email against multiple conditions. */
  def validateEmail(email: String)(conditions: (String => Boolean)*): Boolean =
    conditions.forall(condition => condition(email))

  /** Validates an email using a regex pattern. */
  def validateEmailWithRegex(email: String): Boolean = {
    val emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$".r
    emailRegex.findFirstIn(email).isDefined
  }
}
