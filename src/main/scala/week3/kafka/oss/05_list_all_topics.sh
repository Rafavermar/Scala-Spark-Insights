#!/bin/bash
echo Listando Topics
. ./env.sh

$dir_kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

