#!/usr/bin/env bash

# Remove previous docker image
docker rm -f spark.scala/sparkeoi:latest

cd ..

# Build the docker image
sbt docker:publishLocal

docker images | grep spark-scala

# executing just for check everything went ok.
docker run -d --name spark-scala spark.scala/spark-scala:latest

cd -