package week1.traits
/**
 * Use of traits to implement behaviors in animals, showcasing
 * polymorphism and inheritance in Scala.
 *
 * Traits here function similarly to Java's interfaces, providing a means to define and
 * extend functionalities:
 * - Like Java interfaces, traits define method signatures that must be implemented.
 * - Traits can be mixed into any class, providing greater flexibility compared to Java interfaces.
 * - They support default implementations, which Java only supported starting with version 8.
 *
 * This example leverages traits to add diverse behaviors (like running and flying) to animal classes,
 * illustrating the power of Scala's rich type system compared to traditional Java interfaces.
 */


/**
 * Abstract class to define basic properties and behaviors of animals.
 *
 * @param name the name of the animal.
 */

abstract class Animal(val name: String) {
  val sound: String
}

trait Runner {
  def run(): String = "I am running!"
}

trait Flyer {
  def fly(): String = "I am flying!"
}

/**
 * A Dog class that extends Animal and mixes in the Runner trait.
 */
class Dog(override val name: String, override val sound: String = "Bark") extends Animal(name) with Runner {
  override def run(): String = s"$name says $sound while running!"
}

/**
 * A Bird class that extends Animal and mixes in the Flyer trait.
 */
class Bird(override val name: String, override val sound: String = "Tweet") extends Animal(name) with Flyer {
  override def fly(): String = s"$name says $sound while flying!"
}

object AnimalBehaviors extends App {
  val dog = new Dog("Buddy")
  val bird = new Bird("Sky")

  println(dog.run())
  println(bird.fly())
}
