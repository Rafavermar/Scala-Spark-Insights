package week2.sparksql.utils

object MemoryImplicits {
  implicit class IntToMemorySize(value: Int) {
    def Gb: String = s"${value}g"
  }
}
