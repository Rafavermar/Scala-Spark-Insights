package week1.collections

import scala.collection.mutable

/**
 * Provides examples of mutable collections in Scala.
 * Mutable collections allow in-place modification, such as adding, removing,
 * or updating elements, which can be useful for algorithms that require
 * changeable data storage.
 */
object MutableCollections {
  // A buffer that supports fast addition and removal of integers
  val numbersBuffer: mutable.Buffer[Int] = mutable.Buffer(1, 2, 3, 4, 5)

  // A set of integers allowing easy addition and deletion
  val numbersSet: mutable.Set[Int] = mutable.Set(1, 2, 3, 4, 5)

  // A map that can be updated or extended with new key-value pairs
  val capitalsMap: mutable.Map[String, String] = mutable.Map("France" -> "Paris", "Italy" -> "Rome")

  def main(args: Array[String]): Unit = {
    println("Mutable Buffer: " + numbersBuffer)
    println("Mutable Set: " + numbersSet)
    println("Mutable Map: " + capitalsMap)
  }
}
