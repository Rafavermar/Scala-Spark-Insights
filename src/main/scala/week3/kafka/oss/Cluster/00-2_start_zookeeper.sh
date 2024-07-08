#!/bin/sh

. ./env.sh

echo Iniciando Zookeeper
$dir_kafka/bin/zookeeper-server-start.sh $dir_kafka/config/zookeeper.properties &
