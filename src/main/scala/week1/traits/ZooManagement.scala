package week1.traits

/**
 * Utilizes abstract classes and traits to manage a zoo's variety of animals, demonstrating Scala's
 * powerful object-oriented features combined with functional programming concepts.
 *
 * In this Scala context:
 * - Abstract classes are similar to Java's abstract classes but can be used with traits for more complex behavior combinations.
 * - Traits here are used like enhanced interfaces with the ability to hold state and have concrete implementations,
 *   providing functionalities that go beyond what Java interfaces offer before Java 8.
 * - The flexibility of traits and abstract classes together facilitates more expressive designs than possible with Java alone.
 *
 * This setup showcases how Scala can be used for effective and maintainable object-oriented programming with a functional twist.
 */

/**
 * An abstract class to define the basic framework of any animal in the zoo.
 */

abstract class ZooAnimal(val name: String) {
  def makeSound(): String
}


trait Terrestrial {
  def walk(): String = "I am walking on the ground."
}


trait Aquatic {
  def swim(): String = "I am swimming in the water."
}


class Elephant(override val name: String) extends ZooAnimal(name) with Terrestrial {
  def makeSound(): String = s"$name makes a trumpet sound."
}


class Dolphin(override val name: String) extends ZooAnimal(name) with Aquatic {
  def makeSound(): String = s"$name clicks and whistles."
}

object ZooManagement extends App {
  val elephant = new Elephant("Dumbo")
  val dolphin = new Dolphin("Flipper")

  println(elephant.makeSound())
  println(elephant.walk())
  println(dolphin.makeSound())
  println(dolphin.swim())
}

