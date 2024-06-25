package week1.traits

/**
 * Defines a movable point within a 2D space using Scala's trait feature.
 *
 * Traits in Scala are similar to interfaces in Java with some notable differences:
 * - Traits can have fully implemented methods, similar to Java 8's default methods.
 * - Unlike Java interfaces, traits can have state (fields).
 * - Scala allows multiple traits to be mixed into classes, similar to multiple inheritance of behavior but not state.
 *
 * This use of traits shows how to encapsulate behavior in a modular and reusable way, which is conceptually
 * similar to using interfaces in Java to define contracts and provide default method implementations.
 */
trait Movable {
  def move(x: Int, y: Int): Unit
}

/**
 * A class representing a point in a 2D coordinate system.
 * Implements the Movable trait to change its position.
 *
 * @param x initial position on the X-axis.
 * @param y initial position on the Y-axis.
 */
class Point(var x: Int, var y: Int) extends Movable {
  def move(newX: Int, newY: Int): Unit = {
    this.x = newX
    this.y = newY
  }
  override def toString: String = s"($x, $y)"
}

object PointMovable extends App {
  val point = new Point(0, 0)
  println(point.toString)
  point.move(5, 5)
  println(s"After moving: ${point.toString}")
}

