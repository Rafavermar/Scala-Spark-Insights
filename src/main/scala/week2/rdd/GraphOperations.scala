package week2.rdd

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import week2.rdd.SparkSetup.sc

/**
 * The `GraphOperations` object demonstrates the application of GraphX, a Spark API for graphs and graph-parallel computation.
 * Utilizing RDDs (Resilient Distributed Datasets), this object constructs a graph based on defined vertices and edges,
 * performs analysis on the relationships between vertices, and showcases various operations including mapping, filtering,
 * and data frame transformations.
 *
 * Overview:
 * - **Vertex and Edge Definition**: The class begins by defining vertices and edges using Spark's `RDD`. Vertices represent
 *   individuals with unique identifiers and associated names, while edges represent relationships between these individuals
 *   annotated with relationship types (e.g., "isFriend", "knows").
 *
 * - **Graph Construction**: Utilizing these vertices and edges, a GraphX graph is constructed. This graph facilitates
 *   complex relational queries and operations which are pivotal in network analysis, social media analysis, and more.
 *
 * - **Graph Analysis**: The graph is analyzed by examining triplets, which encapsulate the source vertex, the destination
 *   vertex, and the edge connecting them. Each triplet is then printed, showcasing the direct relationships and interactions
 *   within the graph.
 *
 * - **Data Conversion and Display**: Demonstrates the conversion of graph data into a Spark DataFrame format for easier
 *   manipulation and visualization. The DataFrame is then displayed, showing relationships in a tabular format, which is
 *   useful for integration with SQL-based tools or BI platforms.
 *
 * Use Cases:
 * This class is tailored for educational demonstrations of GraphX capabilities within Spark, providing insights into graph
 * construction, traversal, and transformation. It can also serve as a foundational template for projects requiring network
 * analysis, such as social network analysis, recommendation systems, and more complex relational data processing tasks.
 */
object GraphOperations extends App {
  // Define the vertices with some user data (id, name)
  val vertices: RDD[(Long, String)] = sc.parallelize(Seq(
    (1L, "Alice"),
    (2L, "Bob"),
    (3L, "Charlie"),
    (4L, "David"),
    (5L, "Ed"),
    (6L, "Fran")
  ))

  // Define the edges with some relationship data
  val edges: RDD[Edge[String]] = sc.parallelize(Seq(
    Edge(1L, 2L, "isFriend"),
    Edge(2L, 3L, "isFriend"),
    Edge(3L, 4L, "knows"),
    Edge(4L, 5L, "isFriend"),
    Edge(5L, 6L, "isFamily"),
    Edge(6L, 1L, "isFriend")
  ))

  // Construct the graph
  val graph: Graph[String, String] = Graph(vertices, edges)

  // Print out each triplet (relationships between vertices)
  graph.triplets.map(triplet =>
    s"${triplet.srcAttr} ${triplet.attr} ${triplet.dstAttr}"
  ).collect().foreach(println)

  // Convert graph to DataFrame and show
  import SparkSetup.spark.implicits._
  graph.triplets.map(triplet =>
    (triplet.srcId, triplet.dstId, triplet.attr)
  ).toDF("src", "dst", "relationship").show()

  SparkSetup.stop()
}