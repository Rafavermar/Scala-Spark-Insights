## Let's see how we can use GROUPING SETS and ROLLUP in Spark SQL to perform more advanced aggregation operations.

```scala
object GroupingSetsApp extends App with SparkSessionWrapper {

spark.sparkContext.setLogLevel("ERROR")

// Desactivamos AQE
spark.conf.set("spark.sql.adaptive.enabled", "false")
// Desactivamos el broadcast join
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "-1")

import spark.implicits._

val data = Seq(
("Banana", "Fruit", 1000, 1),
("Carrot", "Vegetable", 1000, 1),
("Bean", "Vegetable", 2000, 2),
("Orange", "Fruit", 2000, 2),
("Banana", "Fruit", 4000, 3),
("Carrot", "Vegetable", 4000, 3),
("Bean", "Vegetable", 3000, 0 ),
("Orange", "Fruit", 3000, 0)
)


val df = data.toDF("Name", "Category", "Sales", "Quantity")

df.createOrReplaceTempView("sales_data")

val result = spark.sql("SELECT Name, Category, sum(Sales) FROM sales_data GROUP BY Name, Category GROUPING SETS((Name, Category))")

// Idem. pero con el método groupingSets
// Usamos rollup para obtener el mismo resultado que con grouping sets
val resultDf = df.groupBy("Name", "Category").agg(functions.sum("Sales")).rollup("Name", "Category").count()

result.explain(true)
resultDf.explain(true);}
````
You will see with these operators that a step is created with the `Expand` operator.

`Expand` is responsible for adding an additional column to the local table output.

In this case, it adds an additional column called `spark_grouping_id` that is used to identify the groups.

```json
== Physical Plan ==
*(2) HashAggregate(keys=[Name#26, Category#27, spark_grouping_id#25L], functions=[sum(Sales#15)], output=[Name#26, Category#27, sum(Sales)#22L])
+- Exchange hashpartitioning(Name#26, Category#27, spark_grouping_id#25L, 200), ENSURE_REQUIREMENTS, [plan_id=20]
+- *(1) HashAggregate(keys=[Name#26, Category#27, spark_grouping_id#25L], functions=[partial_sum(Sales#15)], output=[Name#26, Category#27, spark_grouping_id#25L, sum#32L])
+- *(1) Expand [[Sales#15, Name#23, Category#24, 0]], [Sales#15, Name#26, Category#27, spark_grouping_id#25L]
+- *(1) LocalTableScan [Sales#15, Name#23, Category#24]
```

`+- *(1) Expand` is responsible for adding an additional column to the local table output.

In this case, it adds an additional column called `spark_grouping_id` that is used to identify the groups.

### Which would you say is more optimal?

#### Using GROUPING SETS:

```json
== Physical Plan ==
*(2) HashAggregate(keys=[Name#26, Category#27, spark_grouping_id#25L], functions=[sum(Sales#15)], output=[Name#26, Category#27, sum(Sales)#22L])
+- Exchange hashpartitioning(Name#26, Category#27, spark_grouping_id#25L, 200), ENSURE_REQUIREMENTS, [plan_id=20]
+- *(1) HashAggregate(keys=[Name#26, Category#27, spark_grouping_id#25L], functions=[partial_sum(Sales#15)], output=[Name#26, Category#27, spark_grouping_id#25L, sum#54L])
+- *(1) Expand [[Sales#15, Name#23, Category#24, 0]], [Sales#15, Name#26, Category#27, spark_grouping_id#25L]
+- *(1) LocalTableScan [Sales#15, Name#23, Category#24]
```

#### Using ROLLUP:

```json
== Physical Plan ==
*(3) HashAggregate(keys=[Name#48, Category#49, spark_grouping_id#47L], functions=[count(1)], output=[Name#48, Category#49, count#44L])
+- Exchange hashpartitioning(Name#48, Category#49, spark_grouping_id#47L, 200), ENSURE_REQUIREMENTS, [plan_id=67]
+- *(2) HashAggregate(keys=[Name#48, Category#49, spark_grouping_id#47L], functions=[partial_count(1)], output=[Name#48, Category#49, spark_grouping_id#47L, count#57L])
+- *(2) Expand [[Name#13, Category#14, 0], [Name#13, null, 1], [null, null, 3]], [Name#48, Category#49, spark_grouping_id#47L]
+- *(2) HashAggregate(keys=[Name#13, Category#14], functions=[], output=[Name#13, Category#14])
+- Exchange hashpartitioning(Name#13, Category#14, 200), ENSURE_REQUIREMENTS, [plan_id=61]
+- *(1) HashAggregate(keys=[Name#13, Category#14], functions=[], output=[Name#13, Category#14])
+- *(1) LocalTableScan [Name#13, Category#14]
```

Both physical plans perform similar grouping and aggregation operations, and both require comparable amounts
processing and memory resources.

However, there are some key differences that could influence processing efficiency depending on the size and
nature of your data:

- The plan with `GROUPING SETS` performs a single partial aggregation in stage 1.
  - The `partial_sum` function is calculated for each different combination of Name and Category.
  - Then, in stage 2, a final aggregation is performed to combine the partial results.
- The plan with `ROLLUP`, performs an additional transformation `Expand` in stage 2 that generates the different
  combinations of values for Name and Category.
  - This additional step may take more time and memory, especially if you have a large number of combinations
    different.
  - However, the plan with `ROLLUP` can also be more efficient in some cases, as it can reduce the
    amount of data that must be processed in the final aggregation stage.
- Both plans involve an `Exchange` operation, which distributes data between partitions based on the
  grouping key values. If you have a large amount of data, this could impact performance, as
  An `Exchange` operation can involve expensive data transfer over the network.
- Generally, for large data sets, `GROUPING SETS` can be more efficient than `ROLLUP` due to
  that `ROLLUP` requires an additional stage of data expansion.
- However, the relative efficiency of these two methods may depend on other factors as well, such as the
  distribution of your data and the configuration of your Spark cluster.

## Data Bucketization

```scala
object GroupingSets02App extends App with SparkSessionWrapper {

  spark.sparkContext.setLogLevel("ERROR")

  // Desactivamos AQE
  spark.conf.set("spark.sql.adaptive.enabled", "false")
  // Desactivamos el broadcast join
  spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "-1")

  import spark.implicits._

  val data = Seq(
    ("Banana", "Fruit", 1000, 1),
    ("Carrot", "Vegetable", 1000, 1),
    ("Bean", "Vegetable", 2000, 2),
    ("Orange", "Fruit", 2000, 2),
    ("Banana", "Fruit", 4000, 3),
    ("Carrot", "Vegetable", 4000, 3),
    ("Bean", "Vegetable", 3000, 0),
    ("Orange", "Fruit", 3000, 0)
  )

  // Generamos usa collección de datos enorme a partir de la colección de datos anterior
  // La función flatMap aplica la función que recibe a cada elemento de la colección y devuelve una colección de elementos
  val data2 = (0 until 1000000).flatMap { _ =>
    data
  }

  val BucketSize = 4
  val BucketColumn = "Name"
  // Seguimos el critrio de los NoSQL y usamos el nombre de la tabla como el nombre de la columna de bucketing
  val TablaPorNombre = "sales_data_by_name"

  val df = data2.toDF(BucketColumn, "Category", "Sales", "Quantity")

  // Vamos a usar bucketing para mejorar el rendimiento
  df.write.bucketBy(BucketSize, BucketColumn).saveAsTable(TablaPorNombre)

  val dfConBucketing = spark.table(TablaPorNombre)

  val result = spark.sql(s"SELECT $BucketColumn, Category, sum(Sales) FROM $TablaPorNombre GROUP BY $BucketColumn, Category GROUPING SETS(($BucketColumn, Category))")

  // Idem. pero con el método groupingSets
  // Usamos rollup para obtener el mismo resultado que con grouping sets
  val resultDf = dfConBucketing.groupBy(BucketColumn, "Category").agg(functions.sum("Sales")).rollup(BucketColumn, "Category").count()

  result.explain(true)
  resultDf.explain(true)

  result.show()
}
```

### Con GROUPING SETS

```json
== Parsed Logical Plan ==
'Aggregate [groupingsets(Vector(0, 1), 'Name, 'Category, 'Name, 'Category)], ['Name, 'Category, unresolvedalias('sum('Sales), None)]
+- 'UnresolvedRelation [sales_data_by_name], [], false

== Analyzed Logical Plan ==
Name: string, Category: string, sum(Sales): bigint
Aggregate [Name#38, Category#39, spark_grouping_id#37L], [Name#38, Category#39, sum(Sales#27) AS sum(Sales)#34L]
+- Expand [[Name#25, Category#26, Sales#27, Quantity#28, Name#35, Category#36, 0]], [Name#25, Category#26, Sales#27, Quantity#28, Name#38, Category#39, spark_grouping_id#37L]
+- Project [Name#25, Category#26, Sales#27, Quantity#28, Name#25 AS Name#35, Category#26 AS Category#36]
+- SubqueryAlias spark_catalog.default.sales_data_by_name
+- Relation spark_catalog.default.sales_data_by_name[Name#25,Category#26,Sales#27,Quantity#28] parquet

== Optimized Logical Plan ==
Aggregate [Name#38, Category#39, spark_grouping_id#37L], [Name#38, Category#39, sum(Sales#27) AS sum(Sales)#34L]
+- Expand [[Sales#27, Name#25, Category#26, 0]], [Sales#27, Name#38, Category#39, spark_grouping_id#37L]
+- Project [Sales#27, Name#25, Category#26]
+- Relation spark_catalog.default.sales_data_by_name[Name#25,Category#26,Sales#27,Quantity#28] parquet

== Physical Plan ==
*(2) HashAggregate(keys=[Name#38, Category#39, spark_grouping_id#37L], functions=[sum(Sales#27)], output=[Name#38, Category#39, sum(Sales)#34L])
+- Exchange hashpartitioning(Name#38, Category#39, spark_grouping_id#37L, 200), ENSURE_REQUIREMENTS, [plan_id=53]
+- *(1) HashAggregate(keys=[Name#38, Category#39, spark_grouping_id#37L], functions=[partial_sum(Sales#27)], output=[Name#38, Category#39, spark_grouping_id#37L, sum#67L])
+- *(1) Expand [[Sales#27, Name#25, Category#26, 0]], [Sales#27, Name#38, Category#39, spark_grouping_id#37L]
+- *(1) ColumnarToRow
+- FileScan parquet spark_catalog.default.sales_data_by_name[Name#25,Category#26,Sales#27] Batched: true, Bucketed: true, DataFilters: [], Format: Parquet, Location: InMemoryFileIndex(1 paths)[file:/C:/MASTER-DE/SparkEOI/spark-warehouse/sales_data_by_name], PartitionFilters: [], PushedFilters: [], ReadSchema: struct<Name:string,Category:string,Sales:int>, SelectedBucketsCount: 4 out of 4
```

### Con ROLLUP

```json
== Parsed Logical Plan ==
'Aggregate [rollup(Vector(0), Vector(1), Name#25, Category#26)], [Name#25, Category#26, count(1) AS count#57L]
+- Aggregate [Name#25, Category#26], [Name#25, Category#26, sum(Sales#27) AS sum(Sales)#49L]
+- SubqueryAlias spark_catalog.default.sales_data_by_name
+- Relation spark_catalog.default.sales_data_by_name[Name#25,Category#26,Sales#27,Quantity#28] parquet

== Analyzed Logical Plan ==
Name: string, Category: string, count: bigint
Aggregate [Name#61, Category#62, spark_grouping_id#60L], [Name#61, Category#62, count(1) AS count#57L]
+- Expand [[Name#25, Category#26, sum(Sales)#49L, Name#58, Category#59, 0], [Name#25, Category#26, sum(Sales)#49L, Name#58, null, 1], [Name#25, Category#26, sum(Sales)#49L, null, null, 3]], [Name#25, Category#26, sum(Sales)#49L, Name#61, Category#62, spark_grouping_id#60L]
+- Project [Name#25, Category#26, sum(Sales)#49L, Name#25 AS Name#58, Category#26 AS Category#59]
+- Aggregate [Name#25, Category#26], [Name#25, Category#26, sum(Sales#27) AS sum(Sales)#49L]
+- SubqueryAlias spark_catalog.default.sales_data_by_name
+- Relation spark_catalog.default.sales_data_by_name[Name#25,Category#26,Sales#27,Quantity#28] parquet

== Optimized Logical Plan ==
Aggregate [Name#61, Category#62, spark_grouping_id#60L], [Name#61, Category#62, count(1) AS count#57L]
+- Expand [[Name#25, Category#26, 0], [Name#25, null, 1], [null, null, 3]], [Name#61, Category#62, spark_grouping_id#60L]
+- Aggregate [Name#25, Category#26], [Name#25, Category#26]
+- Project [Name#25, Category#26]
+- Relation spark_catalog.default.sales_data_by_name[Name#25,Category#26,Sales#27,Quantity#28] parquet

== Physical Plan ==
*(2) HashAggregate(keys=[Name#61, Category#62, spark_grouping_id#60L], functions=[count(1)], output=[Name#61, Category#62, count#57L])
+- Exchange hashpartitioning(Name#61, Category#62, spark_grouping_id#60L, 200), ENSURE_REQUIREMENTS, [plan_id=104]
+- *(1) HashAggregate(keys=[Name#61, Category#62, spark_grouping_id#60L], functions=[partial_count(1)], output=[Name#61, Category#62, spark_grouping_id#60L, count#70L])
+- *(1) Expand [[Name#25, Category#26, 0], [Name#25, null, 1], [null, null, 3]], [Name#61, Category#62, spark_grouping_id#60L]
+- *(1) HashAggregate(keys=[Name#25, Category#26], functions=[], output=[Name#25, Category#26])
+- *(1) HashAggregate(keys=[Name#25, Category#26], functions=[], output=[Name#25, Category#26])
+- *(1) ColumnarToRow
+- FileScan parquet spark_catalog.default.sales_data_by_name[Name#25,Category#26] Batched: true, Bucketed: true, DataFilters: [], Format: Parquet, Location: InMemoryFileIndex(1 paths)[file:/C:/MASTER-DE/SparkEOI/spark-warehouse/sales_data_by_name], PartitionFilters: [], PushedFilters: [], ReadSchema: struct<Name:string,Category:string>, SelectedBucketsCount: 4 out of 4
```

When data is bucketed by the `Name` column, Spark can optimize some operations since it is
aware of the locality of the data.

In particular, it can reduce the cost of the Shuffle operation during the aggregation process.

Here I reflect some changes that can be seen in the physical plans:

#### GROUPING SETS

On the physical plan, a key difference is that Spark is now using `FileScan` to read data from Parquet.

This is a performance improvement over the regular table, which occurs because the data is organized in buckets
by the `Name` column.

The `FileScan` operation now has the `Bucketed: true` property, indicating that Spark is taking advantage of the
bucketization.

Finally, `SelectedBucketsCount` indicates that Spark is reading data from all buckets (4 out of 4), this means
which is taking advantage of data bucketing to improve performance.

#### ROLLUP

Similar to the case of `GROUPING SETS`, the physical plan now also uses `FileScan` to read Parquet files,
with `Bucketed: true`.

The `HashAggregate` operation is the same in both cases, for `Name` and `Category`.

Again, `SelectedBucketsCount` indicates that Spark is reading all available buckets (4 out of 4).

In short, bucketing data allows Spark to optimize the reading and processing of data by
bucketing allows Spark to 'FileScan' buckets in parallel, which is more efficient than reading the entire bucket.
board. Furthermore, the execution of aggregation operations can be more efficient and faster due to the decrease in
the need for data shuffling in the aggregation phase.

## With Bucketing and Partitioning

```scala
// Tabla con bucketing y particionada por Category
val TablaPorNombreYCategoria = "sales_data_by_name_and_category"
df.write.partitionBy("Category").bucketBy(BucketSize, BucketColumn).saveAsTable(TablaPorNombreYCategoria)

val dfConBucketingYParticionado = spark.table(TablaPorNombreYCategoria)

val result2 = spark.sql(s"SELECT $BucketColumn, Category, sum(Sales) FROM $TablaPorNombreYCategoria GROUP BY $BucketColumn, Category GROUPING SETS(($BucketColumn, Category))")

// Idem. pero con el método groupingSets
// Usamos rollup para obtener el mismo resultado que con grouping sets
val resultDf2 = dfConBucketingYParticionado.groupBy(BucketColumn, "Category").agg(functions.sum("Sales")).rollup(BucketColumn, "Category").count()

result2.explain(true)
resultDf2.explain(true)

result2.show(truncate = false)
resultDf2.show(truncate = false)
```

## The differences that are seen are due to how the data is partitioned:

### GROUPING SETS

```json
== Parsed Logical Plan ==
'Aggregate [groupingsets(Vector(0, 1), 'Name, 'Category, 'Name, 'Category)], ['Name, 'Category, unresolvedalias('sum('Sales), None)]
+- 'UnresolvedRelation [sales_data_by_name_and_category], [], false

== Analyzed Logical Plan ==
Name: string, Category: string, sum(Sales): bigint
Aggregate [Name#111, Category#112, spark_grouping_id#110L], [Name#111, Category#112, sum(Sales#99) AS sum(Sales)#107L]
+- Expand [[Name#98, Sales#99, Quantity#100, Category#101, Name#108, Category#109, 0]], [Name#98, Sales#99, Quantity#100, Category#101, Name#111, Category#112, spark_grouping_id#110L]
   +- Project [Name#98, Sales#99, Quantity#100, Category#101, Name#98 AS Name#108, Category#101 AS Category#109]
      +- SubqueryAlias spark_catalog.default.sales_data_by_name_and_category
         +- Relation spark_catalog.default.sales_data_by_name_and_category[Name#98,Sales#99,Quantity#100,Category#101] parquet

== Optimized Logical Plan ==
Aggregate [Name#111, Category#112, spark_grouping_id#110L], [Name#111, Category#112, sum(Sales#99) AS sum(Sales)#107L]
+- Expand [[Sales#99, Name#98, Category#101, 0]], [Sales#99, Name#111, Category#112, spark_grouping_id#110L]
   +- Project [Sales#99, Name#98, Category#101]
      +- Relation spark_catalog.default.sales_data_by_name_and_category[Name#98,Sales#99,Quantity#100,Category#101] parquet

== Physical Plan ==
*(2) HashAggregate(keys=[Name#111, Category#112, spark_grouping_id#110L], functions=[sum(Sales#99)], output=[Name#111, Category#112, sum(Sales)#107L])
+- Exchange hashpartitioning(Name#111, Category#112, spark_grouping_id#110L, 200), ENSURE_REQUIREMENTS, [plan_id=230]
   +- *(1) HashAggregate(keys=[Name#111, Category#112, spark_grouping_id#110L], functions=[partial_sum(Sales#99)], output=[Name#111, Category#112, spark_grouping_id#110L, sum#140L])
      +- *(1) Expand [[Sales#99, Name#98, Category#101, 0]], [Sales#99, Name#111, Category#112, spark_grouping_id#110L]
         +- *(1) ColumnarToRow
            +- FileScan parquet spark_catalog.default.sales_data_by_name_and_category[Name#98,Sales#99,Category#101] Batched: true, Bucketed: true, DataFilters: [], Format: Parquet, Location: CatalogFileIndex(1 paths)[file:/C:/MASTER-DE/SparkEOI/spark-warehouse/sales_data_by_name_and_cate..., PartitionFilters: [], PushedFilters: [], ReadSchema: struct<Name:string,Sales:int>, SelectedBucketsCount: 4 out of 4
```

- On the physical plan, the FileScan operation is reading from a location that is partitioned by `Category`. HE
  mentions `Bucketed: true`, indicating that the data is grouped into buckets by `Category`.
- In the Exchange stage, data is distributed based on `Name` and `Category`, potentially reducing the
  number of data that has to be moved, thanks to the partition by `Category`.

### ROLLUP

```json
== Parsed Logical Plan ==
'Aggregate [rollup(Vector(0), Vector(1), Name#98, Category#101)], [Name#98, Category#101, count(1) AS count#130L]
+- Aggregate [Name#98, Category#101], [Name#98, Category#101, sum(Sales#99) AS sum(Sales)#122L]
   +- SubqueryAlias spark_catalog.default.sales_data_by_name_and_category
      +- Relation spark_catalog.default.sales_data_by_name_and_category[Name#98,Sales#99,Quantity#100,Category#101] parquet

== Analyzed Logical Plan ==
Name: string, Category: string, count: bigint
Aggregate [Name#134, Category#135, spark_grouping_id#133L], [Name#134, Category#135, count(1) AS count#130L]
+- Expand [[Name#98, Category#101, sum(Sales)#122L, Name#131, Category#132, 0], [Name#98, Category#101, sum(Sales)#122L, Name#131, null, 1], [Name#98, Category#101, sum(Sales)#122L, null, null, 3]], [Name#98, Category#101, sum(Sales)#122L, Name#134, Category#135, spark_grouping_id#133L]
   +- Project [Name#98, Category#101, sum(Sales)#122L, Name#98 AS Name#131, Category#101 AS Category#132]
      +- Aggregate [Name#98, Category#101], [Name#98, Category#101, sum(Sales#99) AS sum(Sales)#122L]
         +- SubqueryAlias spark_catalog.default.sales_data_by_name_and_category
            +- Relation spark_catalog.default.sales_data_by_name_and_category[Name#98,Sales#99,Quantity#100,Category#101] parquet

== Optimized Logical Plan ==
Aggregate [Name#134, Category#135, spark_grouping_id#133L], [Name#134, Category#135, count(1) AS count#130L]
+- Expand [[Name#98, Category#101, 0], [Name#98, null, 1], [null, null, 3]], [Name#134, Category#135, spark_grouping_id#133L]
   +- Aggregate [Name#98, Category#101], [Name#98, Category#101]
      +- Project [Name#98, Category#101]
         +- Relation spark_catalog.default.sales_data_by_name_and_category[Name#98,Sales#99,Quantity#100,Category#101] parquet

== Physical Plan ==
*(2) HashAggregate(keys=[Name#134, Category#135, spark_grouping_id#133L], functions=[count(1)], output=[Name#134, Category#135, count#130L])
+- Exchange hashpartitioning(Name#134, Category#135, spark_grouping_id#133L, 200), ENSURE_REQUIREMENTS, [plan_id=281]
   +- *(1) HashAggregate(keys=[Name#134, Category#135, spark_grouping_id#133L], functions=[partial_count(1)], output=[Name#134, Category#135, spark_grouping_id#133L, count#143L])
      +- *(1) Expand [[Name#98, Category#101, 0], [Name#98, null, 1], [null, null, 3]], [Name#134, Category#135, spark_grouping_id#133L]
         +- *(1) HashAggregate(keys=[Name#98, Category#101], functions=[], output=[Name#98, Category#101])
            +- *(1) HashAggregate(keys=[Name#98, Category#101], functions=[], output=[Name#98, Category#101])
               +- *(1) ColumnarToRow
                  +- FileScan parquet spark_catalog.default.sales_data_by_name_and_category[Name#98,Category#101] Batched: true, Bucketed: true, DataFilters: [], Format: Parquet, Location: CatalogFileIndex(1 paths)[file:/C:/MASTER-DE/SparkEOI/spark-warehouse/sales_data_by_name_and_cate..., PartitionFilters: [], PushedFilters: [], ReadSchema: struct<Name:string>, SelectedBucketsCount: 4 out of 4
```
- At the operations level it is the same as the non-partitioned case. However, as in the case of `GROUPING SETS`, the
  FileScan operation indicates `Bucketed: true`, which means the data is organized into buckets
  by `Category`.
- Also in the case of `ROLLUP`, the `Exchange` stage benefits from the data being partitioned
  by `Category`.

In summary, you should see an improvement in performance thanks to the decrease in the amount of data being moved
in the Exchange operation, thanks to the partition by `Category`.

Choosing whether to prefer `GROUPING SETS` or `ROLLUP` may depend on the size and distribution of the data.
Generally, `GROUPING SETS` can be more efficient as it requires fewer data transformations.