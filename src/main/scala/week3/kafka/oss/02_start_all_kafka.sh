#!/bin/bash
echo Iniciando Zookeeper

# shellcheck disable=SC3046
source env.sh

./00_start_zookeeper.sh
sleep 5
echo Iniciando Kafka
./01_start_kafka.sh
