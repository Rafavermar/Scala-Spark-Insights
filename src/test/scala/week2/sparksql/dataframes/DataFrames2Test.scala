package week2.sparksql.dataframes

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll

class DataFrames2Test extends AnyFunSuite with BeforeAndAfterAll {

  private var spark: SparkSession = _

  override def beforeAll(): Unit = {
    spark = SparkSession.builder().appName("EjemploDFTest").master("local[2]").getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")
  }

  override def afterAll(): Unit = {
    if (spark != null) {
      spark.stop()
    }
  }

  test("DataFrames2Gen generates a DataFrame") {
    val dfGen: DataFrame = DataFrames2Gen.generar(spark)
    assert(dfGen.count() == 1000)
  }

  test("DataFrames2 filters DataFrames correctly") {
    val dfRaw: DataFrame = DataFrames2Gen.generar(spark)
    DataFrames2Gen.guardarCSV(dfRaw, DataFrames2Config.eventosRaw)

    val dfRead: DataFrame = spark.read
      .option("header", "false")
      .option("delimiter", ",")
      .csv(DataFrames2Config.eventosRaw)
      .toDF("evento")

    val dfCorrectos = dfRead.filter(DataFrames2Config.filtroElementosCorrectos)
    val dfIncorrectos = dfRead.filter(DataFrames2Config.filtroElementosIncorrectos)

    assert(dfCorrectos.filter("contains(evento, 'Alerta2')").count() == 0)
    assert(dfIncorrectos.filter("not contains(evento, 'Alerta2')").count() == 0)
  }
}
