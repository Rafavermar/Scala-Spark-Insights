package week1.errors

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._

/**
 * Explores the capabilities of Validated from the Cats library for managing validation processes.
 * Validated is particularly suitable for scenarios where multiple validations need to occur before an
 * operation can proceed, as it allows accumulating errors rather than failing fast like Either.
 *
 * Interaction Context:
 * - While primarily educational, this implementation can be directly applied in systems requiring complex
 *   input validation, interfacing seamlessly with forms processing or configuration verification tasks.
 */
object ErrorHandlingWithValidated extends App {
  def validateRange(value: Int, min: Int, max: Int): Validated[String, Int] =
    if (value >= min && value <= max) Valid(value)
    else Invalid(s"Value $value is outside the range $min to $max")

  println("Validated Example:")
  val rangeValidation = validateRange(10, 5, 15)
  rangeValidation match {
    case Valid(v) => println(s"Validation successful: $v")
    case Invalid(e) => println(s"Validation failed: $e")
  }
}
