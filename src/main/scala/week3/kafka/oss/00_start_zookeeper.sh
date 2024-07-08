#!/bin/bash

. ./env.sh

echo Starting Zookeeper
$dir_kafka/bin/zookeeper-server-start.sh $dir_kafka/config/zookeeper.properties &
