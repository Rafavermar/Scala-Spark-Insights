package week2.sparksql

import org.apache.spark.sql.SparkSession
import week2.sparksql.utils.MemoryImplicits.IntToMemorySize
sealed trait LogLevel {
  def level: String
}
case object ErrorLevel extends LogLevel {
  override def level: String = "ERROR"
}
case object WarnLevel extends LogLevel {
  override def level: String = "WARN"
}
case object InfoLevel extends LogLevel {
  override def level: String = "INFO"
}
case object DebugLevel extends LogLevel {
  override def level: String = "DEBUG"
}
object Log {
  def level(logLevel: LogLevel): Unit = {
    // You can call your 'setLogLevel' function here
    println(s"Setting log level to ${logLevel.level}")
  }
}
trait SparkSessionWrapper {
  implicit val spark: SparkSession = createSparkSession.withLogLevel(InfoLevel).build

  class SessionBuilder {
    private var logLevel: String = "ERROR"
    private var driverMemory: String = "1g"
    private var executorMemory: String = "1g"
    private var driverCores: Int = 1
    private var executorCores: Int = 1
    private var offHeapGbSize: String = "0g"
    private var offHeapEnabled: Boolean = false // Definido correctamente
    private var hiveSupportEnabled: Boolean = false
    private var deltaLakeSupportEnabled: Boolean = false
    private var shufflePartitionsTuned: Boolean = false
    private var appName: String = "Default App Name" // Definido correctamente

    def withLogLevel(level: LogLevel): SessionBuilder = {
      logLevel = level.level
      this
    }
    def withDriverMemory(memory: Int): SessionBuilder = {
      driverMemory = memory.Gb
      this
    }
    def withExecutorMemory(memory: Int): SessionBuilder = {
      executorMemory = memory.Gb
      this
    }
    def withDriverCores(cores: Int): SessionBuilder = {
      driverCores = cores
      this
    }
    def withExecutorCores(cores: Int): SessionBuilder = {
      executorCores = cores
      this
    }
    def withName(name: String): SessionBuilder = {
      appName = name
      this
    }
    def withOffHeapEnabled: SessionBuilder = {
      offHeapEnabled = true
      this
    }

    def withOffHeapGbSize(size: Int): SessionBuilder = {
      offHeapGbSize = size.Gb
      this
    }
    def withEnableHiveSupport: SessionBuilder = {
      hiveSupportEnabled = true
      this
    }
    def withDeltaLakeSupport: SessionBuilder = {
      deltaLakeSupportEnabled = true
      this
    }

    def withTunedShufflePartitions: SessionBuilder = {
      shufflePartitionsTuned = true
      this
    }

    private def buildSparkSession(appName: String): SparkSession = {
      var builder: SparkSession.Builder = SparkSession
        .builder()
        .master("local[*]")
        .appName(appName)
        .config("spark.driver.memory", driverMemory.toString)
        .config("spark.executor.memory", executorMemory.toString)
        .config("spark.driver.cores", driverCores.toString)
        .config("spark.executor.cores", executorCores.toString)
        // Desactivamos la web UI de Spark para evitar problemas con los tests
        .config("spark.ui.enabled", "false")



      if (offHeapEnabled) {
        builder = builder.config("spark.memory.offHeap.enabled", "true")
        builder = builder.config("spark.memory.offHeap.size", offHeapGbSize.toString)
      }

      if (hiveSupportEnabled) {
        builder = builder.enableHiveSupport()
      }
      if (deltaLakeSupportEnabled) {
        builder = builder.config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
        builder = builder.config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
      }

      if (shufflePartitionsTuned) {
        val cores = Runtime.getRuntime.availableProcessors
        builder = builder.config("spark.shuffle.partitions", cores.toString)
      }

      builder.getOrCreate()
    }

    def build: SparkSession = {
      val session = buildSparkSession("spark session")
      setLogLevel(session, logLevel)
      session
    }
    def build(name: String): SparkSession = {
      val session = buildSparkSession(name)
      setLogLevel(session, logLevel)
      session
    }
  }
  def createSparkSession: SessionBuilder = new SessionBuilder
  private def setLogLevel(spark: SparkSession, logLevel: String): Unit = spark.sparkContext.setLogLevel(logLevel)
}