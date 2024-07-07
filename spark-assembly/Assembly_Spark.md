# Spark Assembly

An assembly is a JAR file that contains the classes of an application along with all its dependencies. In the case of Spark, an assembly JAR is a JAR file that includes your code, Spark's dependencies, and your application's dependencies. Assembly JARs are necessary to run Spark applications on a cluster.

## Creating an Assembly JAR

To create an assembly JAR, you can use the `sbt-assembly` tool. To do this, add the following snippet of code to your `build.sbt` file:

```scala
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")
```

Then, execute the following command to create the assembly JAR:

```bash 
sbt assembly
```

```bash 
sbt publish-docker
```