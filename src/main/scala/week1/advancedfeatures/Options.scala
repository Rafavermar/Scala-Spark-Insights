package week1.advancedfeatures

/**
 * Demonstrates the use of Scala's Option type to handle nullable expressions safely,
 * preventing common errors like NullPointerException. The Option type encapsulates an optional value.
 * A value of type Option[T] can be either Some[T] or None, representing existing or missing values, respectively.
 * This is a more functional way to deal with absent values, compared to null references in other languages.
 */
object Options extends App {
  // Safe division function returning Option[Int]
  def divide(x: Int, y: Int): Option[Int] = if (y != 0) Some(x / y) else None

  // Example of using Option to handle possible absence of a value
  val result = divide(10, 0)
  result match {
    case Some(value) => println(s"Result: $value")
    case None => println("Error: Division by zero")
  }

  // Using Option with map to transform the contained value if present
  val mapResult = divide(20, 4).map(_ * 2)  // Should return Some(10)
  println(s"Map result: $mapResult")

  // Using flatMap to chain another division operation that also returns an Option
  val flatMapResult = divide(20, 5).flatMap(res => divide(res, 2))  // Should return Some(2)
  println(s"FlatMap result: $flatMapResult")

  // Using map with a function that could throw an exception if not handled by Option
  val safeSquareRoot: Option[Double] = Some(25.0).map(math.sqrt)
  println(s"Safe square root of 25: $safeSquareRoot")

  // Combining multiple Options with flatMap and map
  val complexCalculation = divide(50, 5).flatMap(n => divide(n, 2)).map(_ + 3)
  println(s"Complex Calculation Result: $complexCalculation")  // Should print Some(10)

  // Filtering an option's value
  val filteredResult = divide(12, 3).filter(_ > 3)
  println(s"Filtered Result: $filteredResult")  // Should print Some(4)

  // Folding an option to either get its value or handle the None case
  val foldResult = divide(30, 3).fold("Division by zero")(_.toString)
  println(s"Folded Result: $foldResult")  // Should print "10"

  // Creating an Option from a nullable type to safely convert it into an Option
  def toOption[A](value: A): Option[A] = Option(value)
  val nullableString: String = null
  val optionString = toOption(nullableString)
  println(s"Option from nullable: $optionString")  // Should print None

  // Example of using Option in a complex computation with for-comprehension
  val forComprehensionResult = for {
    a <- divide(100, 10)
    b <- divide(a, 2)
    c <- divide(b, 5)
  } yield c
  println(s"For-comprehension result: $forComprehensionResult")  // Should print Some(1)
}
