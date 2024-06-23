package week1.advancedfeatures.packageobjects.utils


/**
 * This object demonstrates how to utilize the shared utilities from the `utils` package object.
 * It showcases the practical use of functions, constants, and type aliases defined at the package level,
 * emphasizing the benefits of centralized definitions in package objects for code reuse and organization.
 */
object UsePackageObject extends App {
  // Using the log function to indicate the start of an application.
  log("Starting the application...")

  // Using a package-level constant to report default timeout settings.
  log(s"Default Timeout is $DefaultTimeout milliseconds.")

  // Demonstrating the use of a mathematical function from the package object.
  val number: Double = 3.0
  log(s"The square of $number is ${square(number)}")

  // Utilizing a type alias to create and use a matrix, enhancing readability.
  val matrix: Matrix = List(
    List(1.0, 2.0),
    List(3.0, 4.0)
  )
  log(s"Using matrix: $matrix")
}
  log(s"Using matrix: $matrix")
}