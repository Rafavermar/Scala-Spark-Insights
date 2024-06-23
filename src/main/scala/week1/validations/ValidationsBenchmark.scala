package week1.validations
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit

/**
 * Benchmarks the performance of email validation functions defined in the `Validations` object
 * from `FunctionalProgramming.scala`. This class is crucial for ensuring that the validation logic
 * not only is correct but also performs well under various conditions.
 */
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class ValidationsBenchmark {

  val email: String = "test@example.com"

  @Benchmark
  def benchmarkEmailValidation(): Boolean =
    Validations.validateEmailWithRegex(email)

  @Benchmark
  def benchmarkEmailValidation2(): Boolean = Validations.validateEmail(email)(
    Validations.containsAt,
    Validations.containsDot,
    Validations.endsWithCom,
    Validations.containsNoSpaces
  )
}
