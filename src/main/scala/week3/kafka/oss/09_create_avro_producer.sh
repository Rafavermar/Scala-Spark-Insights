#!/bin/bash
. ./env.sh


# Numero de mensajes a enviar
max_messages=5000
# Tiempo de espera entre mensajes
sleep_time=0
batch_size=5000

# Crear un productor de mensajes en formato AVRO
# Extracted Kafka producer command as constant.
${dir_kafka}bin/kafka-avro-console-producer --broker-list localhost:9092 --topic topic1 --property \"parse.key=true\" --property \"key.separator=:\" --property \"value.schema='{\"type\":\"record\",\"name\":\"myrecord\",\"fields\":[{\"name\":\"f1\",\"type\":\"string\"}]}'\"


