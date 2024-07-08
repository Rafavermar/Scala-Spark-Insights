# Kafka
Repository containing examples and exercises for Apache Kafka and Streaming.

## Directory Structure

- **02_Kafka_Connect**: Contains configurations and utilities for Kafka Connect.
    - `connector-config.json` - Configuration file for Kafka Connect.
    - `file.txt` - Placeholder or example file (currently empty).

- **03_KSQLDB**: Contains scripts and databases for KSQL.
    - `KSQLDB.md` - Markdown file with notes or documentation about KSQLDB setups.

- **Cluster**: Scripts to manage Kafka and Zookeeper services.
    - `00-2_start_zookeeper.sh` - Script to start Zookeeper.
    - `01-2_start_kafka.sh` - Script to start Kafka servers.
    - `02_start_all_kafka.sh` - Script to start all Kafka components.
    - `03_stop_all_kafka.sh` - Script to stop all Kafka components.
    - More scripts for topic and consumer/producer management.

- **confluent-ksqldb-0.29.0**: Specific version directory for Confluent KSQLDB. **JUST DOWNLOAD IT AND EXTRACT HERE**
    - Contains configuration files and scripts pertinent to this version of KSQLDB.

- **kafka_2.13-3.7.0**: Kafka distribution with version 2.13-3.7.0. **JUST DOWNLOAD IT AND EXTRACT HERE**
    - Includes scripts for Zookeeper and Kafka operations similar to those in the `Cluster` directory.

- **Kafka_Administration.md**: Markdown file with Kafka administration notes.

- **env.sh**: Environment variables required for running the project.

- **Intro Apache Kafka v1.0_2G57AWP32.zip**: Archived document or guide for Apache Kafka.

- **Kafka OSS.md**: Markdown file with information about open-source software compliance for Kafka.

- **kill_kafka.sh**: Script to forcefully stop Kafka services.

- **Zookeeper Shell.md**: Markdown documentation or notes on Zookeeper.

## Getting Started

1. **Starting Kafka and Zookeeper:**
   Navigate to the `Cluster` directory and run:
   ```bash
   ./00-2_start_zookeeper.sh
   ./01-2_start_kafka.sh
   ./04_create_topic1.sh
   ./05_list_all_topics.sh
   ./08_create_producer.sh
   ./07_create_consumer.sh
These scripts will start Zookeeper and Kafka respectively.

2. **Using Kafka Connect:**
   Review ```02_Kafka_Connect/connector-config.json```for Kafka Connect configurations.
3. **Exploring KSQLDB:**
   Open ```03_KSQLDB/KSQLDB.md``` for detailed information and scripts related to KSQLDB