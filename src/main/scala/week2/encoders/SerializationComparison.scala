package week2.encoders

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object SerializationComparison {


  def main(args: Array[String]): Unit = {
    // Configurar Spark para utilizar Kryo como el serializador
    val conf = new SparkConf()
      .setAppName("SerializationComparison")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val spark = SparkSession.builder().config(conf).master("local[2]").getOrCreate()

    // Crear una lista de datos de ejemplo
    val data = List((1, "Alice"), (2, "Bob"), (3, "Charlie"), (4, "David"))

    // Crear un RDD a partir de la lista
    val rdd: RDD[(Int, String)] = spark.sparkContext.parallelize(data)

    // Realizar la serialización y medir el tiempo con Kryo
    val startWithKryo = System.currentTimeMillis()

    rdd.map { case (id, name) => (id, name.length) }.collect()

    val endWithKryo = System.currentTimeMillis()

    // Configurar Spark para utilizar el serializador predeterminado (Java)
    conf.set("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
    val sparkWithoutKryo = SparkSession.builder().config(conf).getOrCreate()

    // Crear un RDD con el serializador predeterminado (Java)
    val rddWithoutKryo: RDD[(Int, String)] = sparkWithoutKryo.sparkContext.parallelize(data)

    // Realizar la serialización y medir el tiempo sin Kryo
    val startWithoutKryo = System.currentTimeMillis()
    rddWithoutKryo.map { case (id, name) => (id, name.length) }.collect()

    val endWithoutKryo = System.currentTimeMillis()

    // Calcular el tiempo de ejecución con y sin Kryo

    val timeWithKryo = endWithKryo - startWithKryo
    val timeWithoutKryo = endWithoutKryo - startWithoutKryo

    // Imprimir las métricas
    println(s"Tiempo con Kryo: $timeWithKryo ms")
    println(s"Tiempo sin Kryo: $timeWithoutKryo ms")

    // Detener las sesiones de Spark
    spark.stop()
    sparkWithoutKryo.stop()
  }

}
