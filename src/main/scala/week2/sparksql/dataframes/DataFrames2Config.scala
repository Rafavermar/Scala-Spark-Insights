package week2.sparksql.dataframes

object DataFrames2Config {
  val eventosRaw = "src/main/scala/week2/sparksql/dataframes/data/eventos"
  val eventosCorrectos = "/src/main/scala/week2/sparksql/dataframesdata/data/eventosCorrectos"
  val eventosIncorrectos = "/src/main/scala/week2/sparksql/dataframesdata/data/eventosIncorrectos"
  val nombreColumnaEventos = "evento"
  val filtroElementosCorrectos = s"not contains($nombreColumnaEventos, 'Alerta2')"
  val filtroElementosIncorrectos = s"contains($nombreColumnaEventos, 'Alerta2')"
}
