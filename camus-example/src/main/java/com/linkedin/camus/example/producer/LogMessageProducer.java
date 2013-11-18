package com.linkedin.camus.example.producer;

import com.linkedin.camus.etl.kafka.coders.KafkaAvroMessageEncoder;
import com.linkedin.camus.example.records.DummyLog;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.hadoop.conf.Configuration;

import java.util.Collections;
import java.util.Properties;

/**
 * Write some {@link com.linkedin.camus.example.records.DummyLog} messages to a kafka topic.
 *
 * The {@link com.linkedin.camus.example.schemaregistry.DummySchemaRegistry} must contain the topic
 * in order for the subsequent camus job to load messages.  This means only the topics DUMMY_LOG
 * and DUMMY_LOG_2 will be available unless you modify the registry.
 */
public class LogMessageProducer {

    public static String USAGE = "<java> broker-str topic num-messages";

    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println(USAGE);
            System.exit(1);
        }

        String broker = args[0];
        String topicName = args[1];
        int numMessages = Integer.parseInt(args[2]);

        Properties props = new Properties();

        props.put("metadata.broker.list", broker);
        props.put( KafkaAvroMessageEncoder.KAFKA_MESSAGE_CODER_SCHEMA_REGISTRY_CLASS,
                "com.linkedin.camus.example.schemaregistry.DummySchemaRegistry");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, byte[]> producer = new Producer<String, byte[]>(config);

        KafkaAvroMessageEncoder kafkaAvroMessageEncoder = new KafkaAvroMessageEncoder(topicName, new Configuration());
        kafkaAvroMessageEncoder.init(props, topicName);
        for (int i = 0; i < numMessages; i++) {

            DummyLog dummyLog = DummyLog.newBuilder()
                    .setId(i)
                    .setLogTime(System.currentTimeMillis())
                    .setMuchoStuff(Collections.<CharSequence, CharSequence>singletonMap("Key1", "Val1"))
                    .build();
            byte[] logBytes = kafkaAvroMessageEncoder.toBytes(dummyLog);

            KeyedMessage<String, byte[]> data = new KeyedMessage<String, byte[]>(topicName, logBytes);
            producer.send(data);
        }

        producer.close();
    }
}
