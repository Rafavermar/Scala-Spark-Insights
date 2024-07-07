addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.4.7")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

// The sbt-native-packager plugin is used for building various types of packages in SBT projects.
// It supports creating application packages in formats like Universal (zip, tar), Debian, RPM, Docker, and more.
// This is useful for deploying Scala and Java applications across different environments easily.
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.0")

// The sbt-assembly plugin is used to create a fat JAR of your project.
// A fat JAR contains all its dependencies and configuration files in a single JAR file, making it easier to deploy.
// This is particularly useful for distributing standalone applications or for deploying applications to environments that do not manage dependencies natively.
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.2.0")
