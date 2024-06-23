package week1.basics

import scala.Console.{GREEN, RED, RESET}
import scala.util.{Failure, Success, Try}

/**
 * Demonstrates exception handling and advanced use of options and tries,
 * showcasing foundational Scala error management techniques.
 */
object ErrorHandlingExamples extends App {

  // basic try-catch functionality
  println("basic TRY CATCH:")
  try {
    val result = 10 / 0
    println(s"Result = $result")
  } catch {
    case e: ArithmeticException => println("Caught Error: Division by zero")
  }
  println()


  // try-catch with multiple exceptions
  println("TRY CATCH with multiple exceptions:")
  try {
    val result = 10 / 0
    println(s"Result = $result")
  } catch {
    case e: ArithmeticException => println("Caught Error: Division by zero")
    case e: Exception => println("Caught Error: General exception")
  } finally {
    println("This is the finally block, executed after try/catch.")
  }
  println()


  // Demonstrating the use of Option for safer error handling
  println("Option:")
  def safeDivide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)

  val resultOption1 = safeDivide(10, 2)
  println(s"Safe division result (10 / 2): $resultOption1")
  val resultOption2 = safeDivide(10, 0)
  println(s"Safe division result (10 / 0): $resultOption2")
  println()


  // Matching on Option values
  println("Option with pattern matching:")
  val resultOptionMatch = safeDivide(10, 2) match {
    case Some(value) => s"Division successful, result: $value"
    case None => "Error: Division by zero"
  }
  println(resultOptionMatch)
  println()


  // Using Try to handle exceptions more functionally
  println("Try:")
  def tryDivide(a: Int, b: Int): Try[Int] = Try(a / b)

  val resultTry1 = tryDivide(10, 2)
  println(s"Try division result (10 / 2): $resultTry1")
  val resultTry2 = tryDivide(10, 0)
  println(s"Try division result (10 / 0): $resultTry2")
  println()


  // Pattern matching with Try
  println("Try with pattern matching:")
  val resultTryMatch = tryDivide(10, 2) match {
    case Success(value) => s"Division successful, result: $value"
    case Failure(exception) => s"Caught an exception: ${exception.getMessage}"
  }
  println(resultTryMatch)
  println()


  // Using for-comprehension with Try to handle multiple operations
  println("Try with for-comprehension:")
  val resultTryFor = for {
    a <- tryDivide(10, 2)
    b <- tryDivide(5, 0) // This will fail
    c <- tryDivide(20, 4)
  } yield a + b + c

  resultTryFor match {
    case Success(value) => println(GREEN + s"Successful result: $value" + RESET)
    case Failure(exception) => println(RED + s"Error during computation: ${exception.getMessage}" + RESET)
  }
  println()


  // For-comprehension with guards
  println("Try with for-comprehension and guards:")
  val filteredResultTryFor = for {
    a <- tryDivide(10, 2) if a > 0
    b <- tryDivide(5, 10) if b > 0 // not fails with (50, 5)
    c <- tryDivide(20, 4) if c > 0
  } yield a + b + c

  filteredResultTryFor match {
    case Success(value) => println(GREEN + s"Filtered successful result: $value" + RESET)
    case Failure(exception) => println(RED + s"Filtered error: ${exception.getMessage}" + RESET)
  }
  println()
}
