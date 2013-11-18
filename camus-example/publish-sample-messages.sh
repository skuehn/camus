#!/usr/bin/env bash

USAGE="$0 broker-str num-messages"

if [ $# -ne 2 ]; then
    echo ${USAGE}
    exit 1
fi

KAFKA_BROKER_STR=$1
NUM_MESSAGES=$2

TOPIC=DUMMY_LOG
JAR=target/camus-example-*-SNAPSHOT-shaded.jar
MAIN_CLASS=com.linkedin.camus.example.producer.LogMessageProducer

java \
    -cp ${JAR} \
    ${MAIN_CLASS} \
    ${KAFKA_BROKER_STR} \
    ${TOPIC} \
    ${NUM_MESSAGES}
