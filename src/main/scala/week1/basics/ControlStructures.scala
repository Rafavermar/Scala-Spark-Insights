package week1.basics

/**
 * Demonstrates various control structures in Scala, focusing on conditionals,
 * loops, and pattern matching.
 */
object ControlStructures extends App {

  println("Simple if-else :")
  val number = 5
  val message = if (number > 0) "The number is positive" else "The number is negative"
  println(s"message = $message")

  println("\nMultiple conditions in if-else:")
  val complexMessage = if (number > 0) "The number is positive"
  else if (number < 0) "The number is negative"
  else "The number is zero"
  println(s"complexMessage = $complexMessage")

  println("\nWhile loop :")
  var i = 0
  while (i < 5) {
    println(s"i = $i")
    i += 1
  }

  println("\nDo-while loop :")
  var j = 0
  do {
    println(s"j = $j")
    j += 1
  } while (j < 5)

  println("\nFor loop with range until :")
  for (k <- 0 until 5) println(s"k = $k")

  println("\nFor loop with range to :")
  for (l <- 0 to 5) println(s"l = $l")

  println("\nFor loop with steps :")
  for (m <- 0 to 10 by 2) println(s"m = $m")

  println("\nReverse range for loop :")
  for (n <- 10 to 0 by -2) println(s"n = $n")

  println("\nFor loop with early exit:")
  def breakExample(): Unit = for (o <- 0 to 10) {
    if (o == 5) {
      println("Exiting the loop")
      return
    }
    println(s"o = $o")
  }
  breakExample()

  println("\nNested for loops :")
  for (p <- 0 to 2; q <- 0 to 2) println(s"p = $p, q = $q")

  println("\nFor loop with guards :")
  for (r <- 0 to 10 if r % 2 == 0) println(s"r = $r")

  println("\nFor comprehension with multiple conditions:")
  val filteredList = for {
    v <- 0 to 10 if v % 2 == 0 && v > 5
  } yield v
  println(s"filteredList = $filteredList")

  println("\nPattern matching :")
  val day = 1
  val dayName = day match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case 3 => "Wednesday"
    case 4 => "Thursday"
    case 5 => "Friday"
    case 6 => "Saturday"
    case 7 => "Sunday"
    case _ => "Invalid day"
  }
  println(s"dayName = $dayName")

  println("\nAdvanced pattern matching with guards:")
  val complexMatch = number match {
    case n if n > 0 => "The number is positive"
    case n if n < 0 => "The number is negative"
    case n if n == 0 => "The number is zero"
    case _ => "Invalid number"
  }
  println(s"complexMatch = $complexMatch")
}
