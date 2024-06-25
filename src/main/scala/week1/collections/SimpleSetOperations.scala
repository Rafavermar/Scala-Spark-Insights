package week1.collections

import scala.collection.{immutable, mutable}

/**
 * Demonstrates basic set operations like adding elements, and performing
 * union, intersection, and differences between sets. Additionally, demonstrates
 * usage with immutable and mutable sets.
 */
object SimpleSetOperations extends App {
  /**
   * Adds an element to the set and returns a new set.
   */
  def addElement(set: Set[Int], element: Int): Set[Int] = set + element

  /**
   * Performs union of two sets.
   */
  def unionSets(set1: Set[Int], set2: Set[Int]): Set[Int] = set1 union set2

  /**
   * Finds intersection of two sets.
   */
  def intersectSets(set1: Set[Int], set2: Set[Int]): Set[Int] = set1 intersect set2

  /**
   * Computes difference between two sets.
   */
  def differenceSets(set1: Set[Int], set2: Set[Int]): Set[Int] = set1 diff set2

  /**
   * Demonstrates symmetric difference of two sets.
   */
  def symmetricDifference(set1: Set[Int], set2: Set[Int]): Set[Int] = (set1 diff set2) union (set2 diff set1)

  /**
   * Demonstrates adding elements to a mutable set.
   */
  def addToMutableSet(mSet: mutable.Set[Int], element: Int): mutable.Set[Int] = {
    mSet += element
  }

  /**
   * Demonstrates operations on an immutable set.
   */
  def addToImmutableSet(iSet: immutable.Set[Int], element: Int): immutable.Set[Int] = {
    iSet + element
  }

  // Test data for mutable and immutable sets
  val mutableSet = mutable.Set(1, 2, 3)
  val immutableSet = immutable.Set(4, 5, 6)

  // Function calls for mutable and immutable sets
  println("Mutable Set Before: " + mutableSet)
  println("Mutable Set After Adding 4: " + addToMutableSet(mutableSet, 4))

  println("Immutable Set Before: " + immutableSet)
  println("Immutable Set After Adding 7: " + addToImmutableSet(immutableSet, 7))

  // Existing demonstrations
  val setA = Set(1, 2, 3, 4)
  val setB = Set(3, 4, 5, 6)
  println("Added Element to Set A (2 added): " + addElement(setA, 2))
  println("Union of Set A and Set B: " + unionSets(setA, setB))
  println("Intersection of Set A and Set B: " + intersectSets(setA, setB))
  println("Difference Set A - Set B: " + differenceSets(setA, setB))
  println("Symmetric Difference between Set A and Set B: " + symmetricDifference(setA, setB))
}