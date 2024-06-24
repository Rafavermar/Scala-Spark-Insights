package week1.config

import com.typesafe.config.{Config, ConfigFactory}
import scala.jdk.CollectionConverters._

object ConfigManagement extends App {
  private var config: Config = _

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
  def hasSetting(path: String): Boolean = config.hasPath(path)
}
