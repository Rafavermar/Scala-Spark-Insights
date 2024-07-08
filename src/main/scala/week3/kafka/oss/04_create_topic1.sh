#!/bin/bash
echo Creando topic1 con 5 particiones

. ./env.sh

$dir_kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic1 --partitions 5 --replication-factor 1

#$dir_kafka/bin/zookeeper-shell.sh localhost:2181
#$dir_kafka/bin/zookeeper-shell.sh localhost:2181/brokers/topics/topics1/partitions
#$dir_kafka/bin/zookeeper-shell.sh localhost:2181/brokers/topics/topics1/partitions/partition_0/state

#ls /brokers/topics/

#ls /brokers/topics/topics1/partitions/partition_0/state