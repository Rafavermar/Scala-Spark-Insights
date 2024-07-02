package week2.encoders

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.scheduler.{SparkListener, SparkListenerTaskEnd}
import org.apache.spark.sql.SparkSession

import java.util.concurrent.atomic.AtomicLong

object SerializationComparisonWithMetrics {

  def main(args: Array[String]): Unit = {
    // Configurar Spark para utilizar Kryo como el serializador
    val conf = new SparkConf()
      .setAppName("SerializationComparisonWithMetrics")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val spark = SparkSession.builder().config(conf).master("local[2]").getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    // Crear una lista de datos de ejemplo
    val baseData = List("Alice", "Bob", "Charlie", "David", "Mario", "Ana", "Felipe", "Maria")
    val data = (1 to 1000000).toList.map(i => (i, baseData((i - 1) % baseData.length)))

    // Crear un RDD a partir de la lista
    val rdd: RDD[(Int, String)] = spark.sparkContext.parallelize(data)

    // Iniciar la monitorización del rendimiento
    spark.sparkContext.addSparkListener(new MySparkListener)

    // Realizar la serialización y medir el tiempo con Kryo
    val startWithKryo = System.currentTimeMillis()
    rdd.map { case (id, name) => (id, name.length) }.collect()
    val endWithKryo = System.currentTimeMillis()

    // Detener las sesiones de Spark
    spark.stop()

    // Imprimir las métricas de tiempo
    val timeWithKryo = endWithKryo - startWithKryo
    println(s"Tiempo con Kryo: $timeWithKryo ms")

    // Imprimir las métricas de memoria y CPU
    println(s"Memoria utilizada: ${MySparkListener.memoryUsed} MB")
    println(s"CPU utilizado: ${MySparkListener.cpuUsed} %")
  }
}

class MySparkListener extends SparkListener {
  override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    MySparkListener.memoryUsed.set(taskEnd.taskMetrics.memoryBytesSpilled / (1024 * 1024)) // Convertir a MB
    MySparkListener.cpuUsed.set(taskEnd.taskMetrics.executorCpuTime / 1000000) // Convertir a porcentaje
  }
}

object MySparkListener {
  val memoryUsed: AtomicLong = new AtomicLong(0)
  val cpuUsed: AtomicLong = new AtomicLong(0)
}





