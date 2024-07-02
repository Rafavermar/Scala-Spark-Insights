package week1.implicits

/**
 * Provides implicit classes and types to handle memory sizes with convenience methods for specifying units.
 *
 * This object includes a case class `Memory` to represent memory sizes with units, and an implicit class
 * `IntWithMemorySize` that enriches the `Int` type with methods to easily create `Memory` instances in various units.
 *
 * ==Example Usage==
 * {{{
 * import Implicits._
 * val tenGigabytes = 10.Gb  // Creates a Memory instance representing 10 gigabytes
 * val fiveMegabytes = 5.Mb  // Creates a Memory instance representing 5 megabytes
 * }}}
 *
 * ==Types Provided==
 * - [[MemorySize]]: An alias for `Int`, represents the size of the memory.
 * - [[ITCapacityUnit]]: An alias for `String`, represents the unit of memory capacity (e.g., "g" for gigabytes).
 */
object Implicits {
  /**
   * Represents a memory size with a unit.
   *
   * @param value the numeric value of the memory size
   * @param unit  the unit of the memory size, such as "g" for gigabytes or "m" for megabytes
   */
  final case class Memory(value: MemorySize, unit: ITCapacityUnit) {
    /**
     * Returns a string representation of the memory size combining value and unit.
     * @return a string representation of the memory size, e.g., "10g"
     */
    override def toString: String = s"$value$unit"
  }

  /** Type alias for an integer representing memory size. */
  type MemorySize = Int

  /** Type alias for a string representing information technology capacity units. */
  type ITCapacityUnit = String

  /**
   * Implicit class to add memory unit methods to integers.
   *
   * This allows the creation of [[Memory]] instances using syntax like `10.Gb` directly on integers.
   *
   * @param value the base value of the memory size from which to create [[Memory]] instances
   */
  implicit class IntWithMemorySize(value: MemorySize) {
    /** Creates a [[Memory]] instance representing gigabytes. */
    def Gb: Memory = Memory(value, "g")

    /** Creates a [[Memory]] instance representing megabytes. */
    def Mb: Memory = Memory(value, "m")

    // Add more methods for additional units as needed, e.g., def Kb: Memory = Memory(value, "k")
  }
}
