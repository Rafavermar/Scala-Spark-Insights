package week1.config

import com.typesafe.config.{Config, ConfigFactory}
import scala.jdk.CollectionConverters._

/**
 * Object that manages application configuration settings by loading them from an application.conf file.
 *
 * This object uses the Lightbend Config library to load and access configurations. It provides methods
 * to retrieve specific configuration values, ensuring they are readily available throughout the application.
 *
 * The configuration is loaded only once at the initialization phase to avoid redundant I/O operations and
 * to maintain consistency across different parts of the application.
 *
 * see ConfigManagementTest
 */
object ConfigManagement extends App {
  private var config: Config = _

  /**
   * Initializes the configuration by loading it from the default application.conf file.
   * If the configuration is already loaded, it does not reload it.
   *
   * @return Boolean indicating if the configuration was successfully loaded or not.
   */
  def initConfig(): Boolean = {
    if (config == null) {
      try {
        config = ConfigFactory.load()
        println("Configuration loaded successfully for the first time.")
        true
      } catch {
        case e: Exception =>
          println(s"Failed to load configuration: ${e.getMessage}")
          config = null
          false
      }
    } else {
      true
    }
  }

  initConfig()

  def getAppName: String = config.getString("app.name")

  def getAppVersion: String = config.getString("app.version")

  def getCustomConfig: String = config.getString("app.custom")

  def getIntList: List[Int] = config.getIntList("app.array").asScala.toList.map(_.toInt)

  /**
   * Checks if a specific configuration setting exists.
   *
   * @param path The configuration key to check.
   * @return Boolean indicating if the key exists in the configuration.
   */
  def hasSetting(path: String): Boolean = config.hasPath(path)
}
