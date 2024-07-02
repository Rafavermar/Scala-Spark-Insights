package week2.sparksql.dataframes


import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

object DataFrames2Gen {
  // Define case classes for structured data
  case class Evento(id: Int, nombre: String, fecha: String, valor: Double, tipo: String = "Alerta1") {
    def toCSV: String = s"$id,$nombre,$fecha,$valor,$tipo"
    override def toString: String = toCSV
  }

  case class EventoIncorrecto(id: Int, nombre: String, fecha: String, tipo: String) {
    def toCSV: String = s"$id,$nombre,$fecha,$tipo"
    def eliminarCaracteres(cadena: String): String = cadena.replaceAll("\"", "")
    override def toString: String = eliminarCaracteres(toCSV)
  }

  // Function to format raw strings into CSV with quotes
  val convertToProperCSVUDF: UserDefinedFunction = udf((row: String) => {
    row.split(",").map(field => "\"" + field + "\"").mkString(",")
  })

  // Generate DataFrame with example data
  def generar(spark: SparkSession): DataFrame = {
    import spark.implicits._
    val eventos = (1 to 1000).map { i =>
      if (i % 10 == 0) EventoIncorrecto(i, s"Evento-$i", "2020-01-01", "Alerta2").toString
      else Evento(i, s"Evento-$i", "2020-01-01", math.random()).toString
    }
    spark.sparkContext.parallelize(eventos).toDF("evento")
  }

  // Save DataFrame to CSV
  def guardarCSV(df: DataFrame, path: String): Unit = {
    df.write.mode(SaveMode.Overwrite).option("header", "false").csv(path)
  }

  // Save DataFrame in Parquet format
  def guardar(df: DataFrame, path: String): Unit = {
    df.write.mode(SaveMode.Overwrite).parquet(path)
  }
}
