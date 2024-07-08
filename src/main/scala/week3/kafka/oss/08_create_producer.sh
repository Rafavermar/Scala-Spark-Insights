#!/bin/bash
. ./env.sh


# Numero de mensajes a enviar
max_messages=5000
# Tiempo de espera entre mensajes
sleep_time=0
batch_size=5000

# Extracted Kafka producer command as constant.
PRODUCER_COMMAND="$dir_kafka/bin/kafka-console-producer.sh --batch-size $batch_size --broker-list localhost:9092 --topic topic1 --property \"parse.key=true\" --property \"key.separator=:\""

# desde consola:
# ./kafka_2.13-3.7.0/bin/kafka-console-producer.sh --batch-size $batch_size --broker-list localhost:9092 --topic topic1 --property "parse.key=true" --property "key.separator=:"

# Function to send messages to Kafka
send_kafka_message() {
  local key=$1
  local value=$2
  echo "${key}:${value}" | $PRODUCER_COMMAND
}

# Function to send JSON messages to Kafka
send_json_kafka_message() {
  local key=$1
  local value=$2
  echo "{\"key\":\"${key}\",\"value\":\"${value}\"}" | $PRODUCER_COMMAND
}


send_messages_with_no_batch() {
  for i in $(seq 1 $max_messages)
  do
    echo "Sending message $i"
    send_kafka_message "key$i" "value$i"
    send_json_kafka_message "key$i" "value$i"
    sleep $sleep_time
  done
}


###  En batches

# Function to send messages to Kafka
 send_kafka_messagev2() {
    local batched_message="$1"
    printf "$batched_message" | $dir_kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic topic1 --property "parse.key=true" --property "key.separator=:"
  }

  send_messages_with_batch() {
    # Initialise to empty
    batch=""

    for i in $(seq 1 $max_messages)
    do
      #echo "Adding to batch: key$i:value$i"
      # Join messages
      #echo "$batch"


      if [ $i = 1 ]; then
        batch="key$i:value$i"
        else
        batch="$batch \nkey$i:value$i"
      fi


      batch="$batch \nkey$i:value$i"
      # Calculate batch remainder
      remainder=`expr $i % $batch_size`

      # If batch size is met, send the batch of messages and clear it for the next batch
      if [ $remainder -eq 0 ]
      then
         echo "Sending batch of messages..."
         send_kafka_messagev2 "$batch"
         batch=""
         sleep $sleep_time
      fi
    done

    # This will send any remaining messages
    if [ -n "$batch" ] # note the quotes around $batch
    then
      echo "Sending final batch of messages..."
      send_kafka_messagev2 "$batch"
    fi
  }

send_messages_with_batch
