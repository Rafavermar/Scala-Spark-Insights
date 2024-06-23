
// Importing necessary modules and methods for the examples
import week1.functions.FunctionalProgramming._
import week1.validations.Validations._

println("---- Lambda Function Example ----")
// Using an anonymous lambda function to perform addition
val lambdaSum = sumLambda(10, 5)
println(s"Lambda function result: $lambdaSum")

println("\n---- Higher Order Function (HOF) Example ----")
// Demonstrating a higher-order function by passing a custom function as an argument
def multiply(a: Int, b: Int): Int = a * b
val hofResult = performOperation(5, 2, multiply)
println(s"HOF multiplication result: $hofResult")

println("\n---- Function Returning a Function Example ----")
// Using a function that returns another function to perform multiplication
val tripleResult = multiplyBy(3)(3)
println(s"Triple function result: $tripleResult")

println("\n---- Partial Application Example ----")
// Partially applying a function
val partialSum = addFive(10)
println(s"Partial application result: $partialSum")

println("\n---- List Transformation Example ----")
// Transforming a list by multiplying its elements
val doubledNumbers = multiplyList(List(1, 2, 3, 4, 5))(2)
println(s"Doubled numbers: $doubledNumbers")

println("\n---- List Summation Example ----")
// Summing two lists element-wise
val listOne = List(100, 200, 300)
val listTwo = List(10, 20, 30)
val summedLists = sumLists(listOne, listTwo)
println(s"Element-wise summed lists: $summedLists")

println("\n---- Email Validation Example ----")
// Validating an email address using a curried function
val emailToValidate = "example@test.com"
val isEmailValid = validateEmail(emailToValidate)(containsAt, endsWithCom, containsDot)
println(s"Is '$emailToValidate' a valid email? $isEmailValid")

println("\n---- Comprehensive Email Validation Example ----")
// More detailed email validation using multiple criteria
val detailedValidation = validateEmail(emailToValidate)(containsAt, endsWithCom, containsDot)
println(s"Comprehensive email validation result: $detailedValidation")
