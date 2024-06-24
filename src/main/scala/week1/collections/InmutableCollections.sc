import week1.collections.ImmutableCollections._

// Demonstration of accessing and manipulating immutable collections
println(numbersList(2))  // Accessing the third element
println(capitalsMap("France"))  // Accessing value by key

// Prepending to list, resulting in a new list
val updatedList = 0 :: numbersList
println(updatedList)

// Squaring each element in the list
val squares = numbersList.map(x => x * x)
println(squares)
