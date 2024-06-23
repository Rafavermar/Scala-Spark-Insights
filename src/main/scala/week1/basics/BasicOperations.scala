package week1.basics

/**
 * Demonstrates basic Scala functionalities such as variable declarations,
 * arithmetic operations, and color-coded console outputs, to illustrate foundational
 * concepts in Scala programming.
 */
object BasicOperations extends App {

  val message = "Hello, world"
  var mutableMessage = "Hello, world"
  mutableMessage = "Hello, world 2"
  println(s"Mutable Message: $mutableMessage")

  def sum(a: Int, b: Int): Int = a + b
  val result1 = sum(2, 3)
  println(s"[Sum] The result of adding 2 and 3 is $result1")


  def sumWithPrint(a: Int, b: Int): Int = {
    val result = sum(a, b)
    println(s"[Sum with Print] The sum of $a and $b is $result")
    result
  }
  val result2 = sumWithPrint(2, 3)
  println(s"[Result of Sum with Print] The result of the sum is $result2")


  def sumWithDefault(a: Int, b: Int = 0): Int = a + b
  val result3 = sumWithDefault(5)
  println(s"[Sum with Default] The result of adding 5 and 0 is $result3")


  def sumList(numbers: List[Int]): Int = numbers.sum
  val result4 = sumList(List(1, 2, 3, 4, 5))
  println(s"[Sum List] The result of summing the list is $result4")


  def printMessage(message: String): Unit = println(s"[Print Message] $message")
  printMessage("Hello, world")


  def printColoredMessageIf(message: String, color: String): Unit = {
    val selectedColor = if (color == "red") Console.RED else Console.BLACK
    println(s"[Print Colored Message If] $selectedColor$message${Console.RESET}")
  }
  printColoredMessageIf("Hello, world in red", "red")


  def printColoredMessageIfElse(message: String, color: String): Unit = {
    val selectedColor =
      if (color == "red") Console.RED
      else if (color == "green") Console.GREEN
      else Console.BLUE
    println(s"[Print Colored Message If Else] $selectedColor$message${Console.RESET}")
  }
  printColoredMessageIfElse("Hello, world in green", "green")
  printColoredMessageIfElse("Hello, world in blue", "blue")


  def printMessageInColor(message: String, color: String): Unit = {
    val colors = Map("red" -> Console.RED, "green" -> Console.GREEN, "yellow" -> Console.YELLOW, "blue" -> Console.BLUE)
    val selectedColor = colors.getOrElse(color, Console.BLUE_B)
    println(s"[Print Message in Color] $selectedColor$message${Console.RESET}")
  }
  var color = "yellow"
  printMessageInColor(s"Hello, world in $color", color)
  color = "blue background"
  printMessageInColor(s"Hello, world in $color", color)


  def printMessageWithPatternMatching(message: String, color: String): Unit = {
    val selectedColor = color match {
      case "red" => Console.RED
      case "green" => Console.GREEN
      case "yellow" => Console.YELLOW
      case "blue" => Console.BLUE
      case _ => Console.BLACK
    }
    println(s"[Print Message with Pattern Matching] $selectedColor$message${Console.RESET}")
  }

    // Example of an unimplemented function. Usefull in case of pretending some scalability o not forgetting to implement it in the future
    // TODO : function implementation pending
  def unimplementedFunction(): Unit = ???
}
