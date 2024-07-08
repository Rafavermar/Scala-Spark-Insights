#!/bin/bash
echo Starting Kafka

. ./env.sh

$dir_kafka/bin/kafka-server-start.sh $dir_kafka/config/server.properties &
