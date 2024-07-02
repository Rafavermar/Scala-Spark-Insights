package week1.functions

import org.scalatest.funsuite.AnyFunSuite

class FunctionalProgrammingTest extends AnyFunSuite {

  test("sumLambda should correctly sum two integers") {
    val sumLambda: (Int, Int) => Int = FunctionalProgramming.sumLambda
    assert(sumLambda(2, 3) === 5)
  }

  test("performOperation should correctly apply the given operation to two integers") {
    val result = FunctionalProgramming.performOperation(4, 5, _ + _)
    assert(result === 9)
  }

  test("multiplyBy should return a function that multiplies its input by the specified factor") {
    val triple = FunctionalProgramming.multiplyBy(3)
    assert(triple(3) === 9)
  }

  test("addFive should add 5 to its input") {
    val addFive = FunctionalProgramming.addFive
    assert(addFive(10) === 15)
  }

  test("function composition should correctly multiply each element in a list by the specified factor") {
    val multipliedList = FunctionalProgramming.multiplyList(List(1, 2, 3))(2)
    assert(multipliedList === List(2, 4, 6))
  }

  test("sumLists should correctly sum two lists element-wise") {
    val listOne = List(1, 2, 3)
    val listTwo = List(4, 5, 6)
    val summedLists = FunctionalProgramming.sumLists(listOne, listTwo)
    assert(summedLists === List(5, 7, 9))
  }

}
