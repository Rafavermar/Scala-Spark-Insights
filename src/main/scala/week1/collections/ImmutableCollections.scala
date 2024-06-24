package week1.collections

/**
 * Provides examples of immutable collections in Scala.
 * Immutable collections once created cannot be modified,
 * ensuring that they are safe for concurrent access and
 * consistent through their lifecycle.
 */
object ImmutableCollections {
  // List of integers
  val numbersList: List[Int] = List(1, 2, 3, 4, 5)

  // Set of integers
  val numbersSet: Set[Int] = Set(1, 2, 3, 4, 5)

  // Map with country and capital city pairs
  val capitalsMap: Map[String, String] = Map("France" -> "Paris", "Italy" -> "Rome")

  def main(args: Array[String]): Unit = {
    println("Immutable List: " + numbersList)
    println("Immutable Set: " + numbersSet)
    println("Immutable Map: " + capitalsMap)
  }
}
