#!/bin/bash
echo Borrando topic1

. ./env.sh

$dir_kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic topic1
