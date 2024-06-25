package week1.errors

import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

/**
 * Establishes benchmarks for various error handling methods using the Java Microbenchmark Harness (JMH).
 * This class is tailored to measure the throughput and efficiency of different error handling
 * strategies under simulated stress conditions.
 *
 * Interaction Context:
 * - This benchmarking setup is crucial for evaluating the performance of error handling mechanisms,
 *   which could inform optimizations and enhancements in system architecture or code refactoring.
 */
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
@Warmup(iterations = 3)
@Measurement(iterations = 8)
@State(Scope.Thread)
class ErrorHandlingBenchmark {

  @Benchmark
  def benchmarkOptionHandling(): Unit = {
    def testOption(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)
    testOption(10, 0)
  }

  @Benchmark
  def benchmarkEitherHandling(): Unit = {
    def testEither(a: Int, b: Int): Either[String, Int] =
      if (b == 0) Left("Cannot divide by zero") else Right(a / b)
    testEither(10, 0)
  }
}
