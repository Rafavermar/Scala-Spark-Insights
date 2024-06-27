package week1.generics

/**
 * Demonstrates the use of generics in Scala to create type-safe and reusable components.
 * This file includes various examples of generic classes and methods that provide
 * flexibility and type safety across different use cases.
 */
object Generics extends App {

  /**
   * A generic class that encapsulates a single value of type T.
   * @param value the encapsulated value.
   * @tparam T the type of the value.
   */
  class Box[T](val value: T) {
    def getValue: T = value
  }

  val box: Box[String] = new Box("Hello")
  println(box.getValue) // Output: Hello

  /**
   * Returns the middle element from a given sequence.
   * @param input the sequence from which to extract the middle element.
   * @tparam A the type of elements in the sequence.
   * @return the middle element of the sequence.
   */
  def middle[A](input: Seq[A]): A = input(input.size / 2)

  val list = List(1, 2, 3, 4, 5, 6)
  println(middle(list)) // Output: 4

  val greeting: String = "HelloWorld"
  println(middle(greeting)) // Output: W

  /**
   * A trait representing an entity capable of developing a value of type T.
   * @tparam T the type of value.
   */
  trait Unwrapper[T] {
    def getValue: T
  }

  /**
   * A case class representing an event with optional nested sub-events.
   * @param name the name of the event.
   * @param date the date of the event.
   * @param eventSource the source of the event.
   * @param mainEvent optional nested sub-event.
   */
  case class NestedEvent(
                          name: String,
                          date: String,
                          eventSource: String,
                          mainEvent: Option[NestedEvent]
                        )

  /**
   * A class designed to extract nested events using the Unwrapper trait.
   * @param valor the initial event to extract from.
   */
  class EventsExtractor(valor: NestedEvent) extends Unwrapper[Option[NestedEvent]] {
    def getValue: Option[NestedEvent] = this.valor.mainEvent.fold(Option(this.valor))(mainEvent => Option(mainEvent))
  }

  val OriginalEvent = NestedEvent("Original Event", "2021-01-01", "Event", None)
  val EventsExtractor = new EventsExtractor(OriginalEvent)
  println(EventsExtractor.getValue) // Output: Some(NestedEvent(...))

  val EventWithMainEvent = NestedEvent(
    "Original Event",
    "2021-01-01",
    "Event",
    Option(NestedEvent("Nested Event", "2021-01-01", "Event", None))
  )
  val EventWithMainEventExtractor = new EventsExtractor(EventWithMainEvent)
  println(EventWithMainEventExtractor.getValue) // Output: Some(NestedEvent(...))

  /**
   * A generic class that encapsulates two values of potentially different types.
   * @param value1 the first value.
   * @param value2 the second value.
   * @tparam T the type of the first value.
   * @tparam U the type of the second value.
   */
  class Box2[T, U](val value1: T, val value2: U) {
    def getValue1: T = value1
    def getValue2: U = value2
  }

  val box2: Box2[String, Int] = new Box2("Hello", 1)
  println(box2.getValue1) // Output: Hello
  println(box2.getValue2) // Output: 1

  /**
   * An extension of Caja2 that also provides a string representation of the second value.
   */
  class Box3[T, U](override val value1: T, override val value2: U) extends Box2[T, U](value1, value2) {
    def getValor2AsString: String = value2.toString
  }

  val caja3: Box3[String, Int] = new Box3("Hello", 1)
  println(caja3.getValue1) // Output: Hello
  println(caja3.getValue2) // Output: 1
  println(caja3.getValor2AsString) // Output: "1"
}

