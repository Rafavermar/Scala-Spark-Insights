package week1.errors

import cats.data.Validated.{Invalid, Valid}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ErrorHandlingWithValidatedTest extends AnyFlatSpec with Matchers {

  "validateRange" should "return Valid(value) when value is within the specified range" in {
    val min = 5
    val max = 15
    val testValue = 10

    val result = ErrorHandlingWithValidated.validateRange(testValue, min, max)
    result shouldBe Valid(testValue)
  }

  it should "return Invalid(error message) when value is below the minimum range" in {
    val min = 5
    val max = 15
    val testValue = 3

    val result = ErrorHandlingWithValidated.validateRange(testValue, min, max)
    result shouldBe Invalid(s"Value $testValue is outside the range $min to $max")
  }

  it should "return Invalid(error message) when value is above the maximum range" in {
    val min = 5
    val max = 15
    val testValue = 16

    val result = ErrorHandlingWithValidated.validateRange(testValue, min, max)
    result shouldBe Invalid(s"Value $testValue is outside the range $min to $max")
  }
}
