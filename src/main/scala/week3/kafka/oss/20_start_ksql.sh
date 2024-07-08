#!/bin/bash


export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64



./confluent-ksqldb-0.29.0/bin/ksql-server-start ./confluent-ksqldb-0.29.0/etc/ksqldb/ksql-server.properties
