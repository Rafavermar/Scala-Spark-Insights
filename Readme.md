#  Scala and Spark Insights

## Description
This repository documents my learning progress as a Data Engineering on Scala with Spark. Here, I will update my portfolio weekly with new concepts, code examples, and insights related to Scala and Spark.

## Table of Contents
- [Week 1: Scala Collections, Error Handling, Configuration, and More](#week-1-scala-collections-error-handling-configuration-and-more)
- [Environment Setup](#environment-setup)
- [Additional Resources](#additional-resources)
- [About me](#about-me)

## Week 1: Scala Collections, Error Handling, Configuration, and More
The first week was an intensive dive into Scala, covering a wide range of topics from basic collections to advanced functionalities in error handling and configuration management.

### Topics Covered
1. **Basic Scala Examples** - Foundational Scala concepts. ([View Code](src/main/scala/week1/basics)).
   - BasicOperations - Mutable and Immutable variables, variable declarations, arithmetic operations, and color-coded console outputs
   - ControlStructures - conditionals, loops, and pattern matching.
   - ErrorHandling - exception handling and advanced use of `Option` and `Try`.
   - Var / Val - mutable and immutable state management, the usage of case classes and functional programming techniques such as `tail recursion` and `foldLeft`
2. **Advanced Features** - Options, TypeAliases, PackageObjects ([View Code](src/main/scala/week1/advancedfeatures)).
3. **Scala Collections** - Exploring mutable and immutable collections. ([View Code](src/main/scala/week1/collections)).
4. **Function Compositions** - Utilizing function chaining and compositions. ([View Code](src/main/scala/week1/functions)).
5. **Configuration Management** - Managing app settings with Typesafe Config.
6. **Error Handling** - Strategies to manage errors gracefully.
7. **Generics** - Implementing generics for type-safe code.
8. **Traits and Abstract Classes** - Using traits and abstract classes to design robust systems.
9. **Validation** - 



## Using Scala Class Objects and Worksheets
Scala class objects serve as single instances of their definitions and are often used to store utility functions and constants. They provide a way to group similar functions or properties under a single namespace, which can enhance the modularity and reusability of your code.

Worksheets, on the other hand, are interactive scripting environments that execute Scala code without the need for explicit compilation. They are ideal for experimenting with Scala code, learning the language, and testing out small code snippets quickly. The immediate feedback provided by worksheets helps accelerate learning and aids in the iterative development process.

## Environment Setup
To ensure your Scala and Spark projects run smoothly in IntelliJ IDEA, especially with Java 17, follow these detailed setup instructions:

### Prerequisites
1. **Scala Plugin**: Ensure the Scala plugin is installed and enabled in IntelliJ IDEA.
2. **Java SDK**: Java 17 should be installed. Set up the SDK in IntelliJ to use Java 17.
3. **sbt**: Install sbt, preferably version 1.10, which supports a range of Scala and Spark functionalities.
4. **Scala**: Configure your project to use Scala version 2.13.14.

### IntelliJ IDEA Configuration
1. **Project Settings**:
    - Go to `File > Project Structure`.
    - In `Project Settings > Project`, select the Java 17 SDK.
    - In `Project Settings > Modules`, ensure your module is set to Scala 2.13.14.

2. **Execution Configuration**:
    - Open run and debug configurations: `Run > Edit Configurations`.
    - Add or edit your Scala/Spark application configuration.
    - In the `VM options` section, add the following lines to ensure compatibility of Spark with Java 17:
      ```
      --add-opens java.base/java.lang=ALL-UNNAMED
      --add-opens java.base/java.util=ALL-UNNAMED
      --add-opens java.base/java.io=ALL-UNNAMED
      --add-opens java.base/java.util.concurrent=ALL-UNNAMED
      --add-opens java.base/java.util.concurrent.atomic=ALL-UNNAMED
      --add-opens java.base/java.util.concurrent.locks=ALL-UNNAMED
      --add-opens java.base/java.util.regex=ALL-UNNAMED
      --add-opens java.base/java.util.stream=ALL-UNNAMED
      --add-opens java.base/java.util.function=ALL-UNNAMED
      --add-opens java.base/java.util.jar=ALL-UNNAMED
      --add-opens java.base/java.util.zip=ALL-UNNAMED
      --add-opens java.base/java.util.spi=ALL-UNNAMED
      --add-opens java.base/java.lang.invoke=ALL-UNNAMED
      --add-opens java.base/java.lang.reflect=ALL-UNNAMED
      --add-opens java.base/java.net=ALL-UNNAMED
      --add-opens java.base/java.nio=ALL-UNNAMED
      --add-opens java.base/sun.nio.ch=ALL-UNNAMED
      --add-opens java.base/sun.nio.cs=ALL-UNNAMED
      --add-opens java.base/sun.security.action=ALL-UNNAMED
      --add-opens java.base/sun.util.calendar=ALL-UNNAMED
      --add-opens java.security.jgss/sun.security.krb5=ALL-UNNAMED
      ```
    - Ensure the option "Add dependencies with 'provided' scope to classpath" is checked.


## Additional Resources

- [Scala Documentation](https://scala-lang.org/documentation/)
- [Apache Spark Documentation](https://spark.apache.org/docs/latest/)

## About me

__Rafael Vera Marañón__ - A Master's student in Data Engineering at __Escuela de Organización Industrial (EOI)__.
Connect with me:

<a href="https://www.linkedin.com/in/rafael-vera-mara%C3%B1%C3%B3n/"><img src="https://cdn-icons-png.flaticon.com/512/174/174857.png" width="20" height="20"/> LinkedIn</a><br>
<a href="https://medium.com/me/notifications"><img src="https://cdn-icons-png.flaticon.com/512/2111/2111543.png" width="20" height="20"/> Medium</a><br>
<a href="https://github.com/Rafavermar?tab=repositories"><img src="https://cdn-icons-png.flaticon.com/512/25/25231.png" width="20" height="20"/> GitHub</a>