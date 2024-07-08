#!/bin/sh

# shellcheck disable=SC3046
. ./env.sh

echo Cerrando Kafka
$dir_kafka/bin/kafka-server-stop.sh
# Sleep 5 segundos
sleep 5

echo Cerrando Zookeeper
$dir_kafka/bin/zookeeper-server-stop.sh
