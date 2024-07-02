package week2.sparksql.dataframes

import org.apache.spark.sql.DataFrame

import scala.Console.{BOLD, RESET}

object DataFrames2 extends App {

  import org.apache.spark.sql.SparkSession

  val spark = SparkSession.builder().appName("EjemploDF02").master("local[2]").getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  val dfRaw: DataFrame = {
    // Assuming the CSV file has one column without header
    val dfGen = DataFrames2Gen.generar(spark)
    DataFrames2Gen.guardarCSV(dfGen, DataFrames2Config.eventosRaw)
    spark.read
      .option("header", "false") // Ensuring we handle CSV without headers
      .option("delimiter", ",")
      .csv(DataFrames2Config.eventosRaw)
      .toDF("evento") // Assigning column name right after reading
  }

  // Filtering logic here using the correct column name
  val filtroElementosCorrectos = s"not contains(evento, 'Alerta2')"
  val filtroElementosIncorrectos = s"contains(evento, 'Alerta2')"

  val dfCorrectos = dfRaw.filter(filtroElementosCorrectos)
  val dfIncorrectos = dfRaw.filter(filtroElementosIncorrectos)

  println(BOLD + "Correct Events:" + RESET)
  dfCorrectos.show()

  println(BOLD + "Incorrect Events:" + RESET)
  dfIncorrectos.show()

  // Further processing and merging
  val dfMerged = dfCorrectos.union(dfIncorrectos)
  dfMerged.show()
}