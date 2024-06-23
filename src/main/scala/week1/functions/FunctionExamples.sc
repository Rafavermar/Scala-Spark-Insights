
// Importing necessary modules and methods for the examples
import week1.functions.FunctionalProgramming._
import week1.validations.Validations._

println("---- Lambda Function Example ----")
val lambdaSum = sumLambda(10, 5)
println(s"Lambda function result: $lambdaSum")

println("\n---- Higher Order Function (HOF) Example ----")
def multiply(a: Int, b: Int): Int = a * b
val hofResult = performOperation(5, 2, multiply)
println(s"HOF multiplication result: $hofResult")

println("\n---- Function Returning a Function Example ----")
val tripleResult = multiplyBy(3)(3)
println(s"Triple function result: $tripleResult")

println("\n---- Partial Application Example ----")
val partialSum = addFive(10)
println(s"Partial application result: $partialSum")

println("\n---- List Transformation Example ----")
val doubledNumbers = multiplyList(List(1, 2, 3, 4, 5))(2)
println(s"Doubled numbers: $doubledNumbers")

println("\n---- List Summation Example ----")
val listOne = List(100, 200, 300)
val listTwo = List(10, 20, 30)
val summedLists = sumLists(listOne, listTwo)
println(s"Element-wise summed lists: $summedLists")

println("\n---- Email Validation Example ----")
val emailToValidate = "example@test.com"
val isEmailValid = validateEmail(emailToValidate)(containsAt, endsWithCom, containsDot)
println(s"Is '$emailToValidate' a valid email? $isEmailValid")

println("\n---- Comprehensive Email Validation Example ----")
val detailedValidation = validateEmail(emailToValidate)(containsAt, endsWithCom, containsDot)
println(s"Comprehensive email validation result: $detailedValidation")
