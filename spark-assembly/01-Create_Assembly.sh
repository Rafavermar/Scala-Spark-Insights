#!/usr/bin/env bash

# check the correct JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-1.21.0-openjdk-amd64/
export PATH=$JAVA_HOME/bin:$PATH

#export TEMP=./tmp
#export TMP=./tmp
#export TMPDIR=./tmp

rm -rf ../target
rm -rf ../project/target
rm -rf ../project/project

cd ..
sbt assembly

ls -lh target/scala-2.13/Scala-Spark-Insights.jar


# execute the jar with spark-submit
# To do this it is needed to download spark in your computer first to a certain directory with permissions.
c:/spark-3.5.0/bin/spark-submit --class src/main/scala/week2/datasetsapi/DatasetsExApp03 --executor-memory 2g --master local[2] target/scala-2.13/Scala-Spark-Insights.jar

cd -