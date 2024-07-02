package week2.sparksql
import org.apache.spark.sql.SparkSession

trait SparkSessionTestWrapper {

  lazy val spark: SparkSession = {
    val session = SparkSession
      .builder()
      .master("local")
      .appName("spark session")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.ui.enabled", "false")
      .config("spark.sql.autoBroadcastJoinThreshold", "-1")
      .config("spark.dirs", "/tmp/spark-temp")
      .config("spark.sql.warehouse.dir", "file:///tmp/spark-warehouse")
      .config("spark.sql.catalogImplementation", "in-memory")
      .config("compression.enabled", "false")
      .getOrCreate()

    session.sparkContext.setLogLevel("ERROR")
    session
  }

}
