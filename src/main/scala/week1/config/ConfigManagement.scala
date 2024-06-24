package week1.config

import com.typesafe.config.ConfigFactory

import scala.jdk.CollectionConverters._

/**
 * Manages application configurations using the Typesafe Config library.
 * It provides methods to retrieve configuration values defined in application.conf.
 * This object demonstrates how application-wide settings can be centralized and accessed.
 */
object ConfigManagement {
  private val config = ConfigFactory.load()

  /** Retrieves the application name from the config. */
  def getAppName: String = config.getString("app.name")

  /** Retrieves the application version from the config. */
  def getAppVersion: String = config.getString("app.version")

  /** Simulates some process, always returning true. */
  def doSomeStuff(): Boolean = {
    println("Doing some stuff")
    true
  }

  /** Retrieves a custom configuration string for demonstration purposes. */
  def getCustomConfig: String = config.getString("app.custom")

  /** Retrieves a list of integers from the configuration. */
  def getIntList: List[Int] = config.getIntList("app.array").asScala.toList.map(_.toInt)

  /** Determines if a specific configuration setting exists. */
  def hasSetting(path: String): Boolean = config.hasPath(path)
}
