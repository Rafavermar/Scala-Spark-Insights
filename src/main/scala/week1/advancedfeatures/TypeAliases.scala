package week1.advancedfeatures

/**
 * Demonstrates how type aliases can be used in Scala to simplify complex type definitions,
 * improving code readability and maintainability. Type aliases are especially useful in complex
 * domain models or functional programming constructs to clarify the types used.
 * Examples for operations on data structures like maps and processing collections are included as well.
 */
object TypeAliases extends App {
  // Type aliases for collection types
  type Matrix = List[List[Int]]
  type Vector = List[Int]
  type UserID = String
  type AccountBalance = Double
  type UserAccounts = Map[UserID, AccountBalance]

  // Simplified matrix creation using type alias
  val matrix: Matrix = List(List(1, 2), List(3, 4))
  val vector: Vector = List(1, 2, 3, 4)

  // Example of using Map with type aliases
  val accounts: UserAccounts = Map("user1" -> 1000.50, "user2" -> 1500.75)

  // Function to add a new user account
  def addAccount(accounts: UserAccounts, userId: UserID, balance: AccountBalance): UserAccounts =
    accounts + (userId -> balance)

  // Adding a new account and printing updated accounts
  val updatedAccounts = addAccount(accounts, "user3", 3000.00)
  println(s"Updated Accounts: $updatedAccounts")

  // Printing matrix and performing operations on vectors
  println(s"Matrix: $matrix")
  println(s"Vector sum: ${vector.sum}")
  println(s"User1's balance: ${accounts("user1")}")

  // Type aliases for function types
  type IntToInt = Int => Int

  // Function definitions using the type alias
  val addOne: IntToInt = _ + 1
  val double: IntToInt = _ * 2
  val subtractOne: IntToInt = _ - 1

  // Combining functions using andThen
  val compute: IntToInt = addOne andThen double andThen subtractOne

  // Usage of combined functions
  println(s"Compute for 5: ${compute(5)}") // Outputs: Compute for 5: 11
  println(s"Compute for 6: ${compute(6)}") // Outputs: Compute for 6: 13
  println(s"Compute for 7: ${compute(7)}") // Outputs: Compute for 7: 15
}
