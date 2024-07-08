# Kafka directories

- `bin`: contains kafka scripts.
    - `kafka-topics.sh`:  Script to create, list, and describe topics.
    - `kafka-console-producer.sh`: Script to create a producer in console mode.
    - `kafka-console-consumer.sh`: Script to create a consumer in console mode.
    - `kafka-server-start.sh`: Script to start the Kafka server.
    - `kafka-server-stop.sh`: Script to stop the Kafka server.
    - `kafka-run-class.sh`: Script to run a Java class.
    - `kafka-configs.sh`: Script to modify Kafka configuration.
    - `kafka-acls.sh`: Script to manage access control lists.
    - `kafka-broker-api-versions.sh`: Script to show the broker API versions.
    - `kafka-consumer-groups.sh`: Script to manage consumer groups.
    - `kafka-consumer-offset-checker.sh`: Script to check consumer offsets.
    - `kafka-consumer-perf-test.sh`:  Script to perform consumer performance tests.
    - `kafka-log-dirs.sh`: Script to show Kafka log directories.
    - `kafka-mirror-maker.sh`:  Script to create a Kafka mirror..
- `config`: Contains Kafka configuration files.
    - `zookeeper.properties`: Zookeeper configuration file.
    - `server.properties`: Kafka server configuration file.
    - `producer.properties`: Kafka producer configuration file.
    - `consumer.properties`: Kafka consumer configuration file.
    - `connect-distributed.properties`: Kafka Connect configuration file.
    - `connect-standalone.properties`: Kafka Connect configuration file in standalone mode
    - `connect-log4j.properties`: Log4j configuration file for Kafka Connect.
    - `connect-runtime.properties`: Kafka Connect runtime configuration file.
- `libs`: Contains Kafka libraries.
- `logs`:  Contains Kafka logs.
- `site-docs`: : Contains Kafka documentation.
- `zookeeper`:Contains Zookeeper files.
- `zookeeper-data`: Contains Zookeeper data.
- `zookeeper-logs`:  Contains Zookeeper logs.

# Kafka Administration

## Introduction

Apache Kafka is a distributed streaming platform used to publish and subscribe to streams of records, similar to a message queue or messaging system. It is fast, scalable, and durable.
## Installation

### Download

```shell
wget https://downloads.apache.org/kafka/2.8.0/kafka_2.13-3.7.0.tgz
```

### Decompression

```shell
tar -xvzf kafka_2.13-3.7.0.tgz
```

### Start

```shell
cd kafka_2.13-3.7.0
```
#### Zookeeper
We are going to start the Zookeeper server, since Kafka depends on it.
```shell
bin/zookeeper-server-start.sh config/zookeeper.properties
```
#### Kafka broker
Starting kafka broker

```shell
bin/kafka-server-start.sh config/server.properties
```

## Commands

### Create a Topic

```shell
bin/kafka-topics.sh --create --topic topics1 --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### List topics

```shell
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### Describe a topic

```shell
bin/kafka-topics.sh --describe --topic topics1 --bootstrap-server localhost:9092
```

### Messages creation

```shell
bin/kafka-console-producer.sh --topic topics1 --bootstrap-server localhost:9092
```

After executing the previous command, a terminal will open where we can write the messages we want to send to the topic `topics1`.

If we want to send a message with a key, we can do it in the following way:

```shell
bin/kafka-console-producer.sh --topic topics1 --bootstrap-server localhost:9092 --property "parse.key=true" --property "key.separator=:"
```
After executing the previous command, a terminal will open where we can write the messages we want to send to the topic `topics1`. In this case, the messages must have a key and a value separated by `:`.
```shell
key1:valor1
key2:valor2
```

### Consume message

```shell
bin/kafka-console-consumer.sh --topic topics1 --from-beginning --bootstrap-server localhost:9092
```

### Delete a topic

```shell
bin/kafka-topics.sh --delete --topic topics1 --bootstrap-server localhost:9092
```

## Consumer groups

A consumer group is a set of consumers that work together to consume messages from one or more topics. Each consumer group has a unique identifier, and each consumer within the group has a unique identifier.
### Create a consumer group

The creation of a consumer group is automatically done when a consumer subscribes to a topic for the first time.
```shell
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --create --group group1
```

### List consumer groups

```shell
bin/kafka-consumer-groups.sh --list --bootstrap-server localhost:9092
```

### Describe a consumers group

```shell
bin/kafka-consumer-groups.sh --describe --group group1 --bootstrap-server localhost:9092
```

### Delete a consumers groups

```shell
bin/kafka-consumer-groups.sh --delete --group group1 --bootstrap-server localhost:9092
```


