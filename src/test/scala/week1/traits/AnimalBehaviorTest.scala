package week1.traits

import org.scalatest.funsuite.AnyFunSuite

class AnimalBehaviorTest extends AnyFunSuite {

  test("Dog should run with its specific behavior") {
    val dog = new Dog("Buddy")
    assert(dog.run() === "Buddy says Bark while running!")
  }

  test("Bird should fly with its specific behavior") {
    val bird = new Bird("Sky")
    assert(bird.fly() === "Sky says Tweet while flying!")
  }

}
