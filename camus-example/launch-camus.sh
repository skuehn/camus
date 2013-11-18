#!/usr/bin/env bash

USAGE="$0 kafka-root-dir kafka-broker-str"

if [ $# -ne 2 ]; then
    echo ${USAGE}
    exit 1
fi

KAFKA_HDFS_ROOT=$1
BROKER=$2
JAR=target/camus-example-*-SNAPSHOT-shaded.jar

hadoop jar \
    ${JAR} \
    -p camus.properties \
    -D etl.destination.path=${KAFKA_HDFS_ROOT}/data-output \
    -D etl.execution.base.path=${KAFKA_HDFS_ROOT} \
    -D etl.execution.history.path=${KAFKA_HDFS_ROOT}/history \
    -D kafka.brokers=${BROKER}
