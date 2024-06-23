package week1.basics

import scala.annotation.tailrec

/**
 * This Scala class demonstrates various Scala concepts through simple examples.
 * The examples highlight mutable and immutable state management, the usage of case classes,
 * and functional programming techniques such as tail recursion and foldLeft.
 */

object VarValExamples extends App {

  // Example 1: Modifying a mutable variable
  var number = 5
  number = 10
  println(s"Mutable variable example: number = $number")
  println()


  // Example 2: Modifying a mutable attribute of an object
  class Person(var name: String)

  val person = new Person("Juan")
  person.name = "Pedro"
  println(s"Mutable attribute example: person.name = ${person.name}")
  println()


  // Example 3: Attempt to modify an immutable attribute of an object
  class ImmutablePerson(val name: String)

  val immutablePerson = new ImmutablePerson("Juan")
  // immutablePerson.name = "Pedro" // This would cause a compilation error
  println("Immutable attribute example: Cannot change 'name' as it is val")
  println()


  // Example 4: Using a new object to change immutable attribute
  class PersonWithNewObject(val name: String)

  val originalPerson = new PersonWithNewObject("Juan")
  val updatedPerson = new PersonWithNewObject("Pedro")
  println(s"Immutable attribute with new object example: originalPerson.name = ${originalPerson.name}")
  println(s"Immutable attribute with new object example: updatedPerson.name = ${updatedPerson.name}")
  println()


  // Example 5: Copying values with a case class
  case class PersonWithCopy(name: String, age: Int)

  val person5 = PersonWithCopy("Juan", 30)
  val person6 = person5.copy(name = "Pedro")
  println(s"Copying with case class example: person5.name = ${person5.name}, person5.age = ${person5.age}")
  println(s"Copying with case class example: person6.name = ${person6.name}, person6.age = ${person6.age}")
  println()


  // Example 6: Modifying multiple attributes using copy in a case class
  val person7 = PersonWithCopy("Juan", 30)
  val person8 = person7.copy(name = "Pedro", age = 40)
  println(s"Multiple modifications with copy example: person7.name = ${person7.name}, person7.age = ${person7.age}")
  println(s"Multiple modifications with copy example: person8.name = ${person8.name}, person8.age = ${person8.age}")
  println()


  // Example 7: Basic function to sum two numbers using a mutable variable
  def sum(a: Int, b: Int): Int = {
    var result = 0
    if (a > 0 && b > 0) result = a + b
    result
  }

  val sumResult = sum(2, 3)
  println(s"Basic sum function using var: result = $sumResult")
  println()


  // Example 8: Summing a list of numbers with tail recursion
  def advancedSumList(numbers: List[Int]): Int = {
    @tailrec
    def sumRecursively(items: List[Int], accumulator: Int): Int = items match {
      case Nil => accumulator
      case head :: tail => sumRecursively(tail, accumulator + head)
    }

    sumRecursively(numbers, 0)
  }

  val advancedSumListResult = advancedSumList(List(1, 2, 3, 4, 5))
  println(s"Advanced sum list with tail recursion: result = $advancedSumListResult")
  println()


  // Example 9: Demonstrating folding over a list to sum its elements, showcasing functional programming capabilities without mutable state.
  def foldSumList(numbers: List[Int]): Int = numbers.foldLeft(0)(_ + _)

  val foldSumListResult = foldSumList(List(1, 2, 3, 4, 5))
  println(s"Fold sum list: result = $foldSumListResult")
  println()
}
