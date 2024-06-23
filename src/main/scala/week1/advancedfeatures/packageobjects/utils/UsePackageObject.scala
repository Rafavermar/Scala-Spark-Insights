package week1.advancedfeatures.packageobjects.utils


/**
 * This object demonstrates how to utilize the shared utilities from the `utils` package object.
 * It showcases the practical use of functions, constants, and type aliases defined at the package level,
 * emphasizing the benefits of centralized definitions in package objects for code reuse and organization.
 */
object UsePackageObject extends App {

  log("Starting the application...")


  log(s"Default Timeout is $DefaultTimeout milliseconds.")

  val number: Double = 3.0
  log(s"The square of $number is ${square(number)}")

  val matrix: Matrix = List(
    List(1.0, 2.0),
    List(3.0, 4.0)
  )
  log(s"Using matrix: $matrix")

  val exampleMatrix: Matrix = List(List(1.0, 2.0), List(3.0, 4.0))
  log(s"Determinant of the matrix: ${determinant(exampleMatrix)}")

  log(s"Sample user profile: ${sampleProfile}")
}
