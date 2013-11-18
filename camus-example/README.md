### Running the example

1. Build the camus jars.  Run `mvn clean install` from the root camus
directory (one level above this directory).  Maven version 3+ is required.
2. Make sure Kafka is up and running.  Make note of the
connection string to your broker, 'localhost:9092' for example. Export
your broker string to the enviroment. run `export
BROKER_STR=localhost:9092`, substituting your broker string in this example.
3. Load some messages. This step will generate N `DummyLog` messages,
encode them in binary avro format, and submit them to the Kafka
'DUMMY_LOG' topic.  Run `./publish-sample-messages.sh ${BROKER_STR}
10` to write 10 dummy log messages to kafka.
4. Run camus.  This step will launch a camus job that copies messages
from the Kafka 'DUMMY_LOG' to an avro container in HDFS. You must
specify the root directory for storing avro containers in HDFS, which
is '/ingest/kafka-etl' in this example.  run:
`./launch-camus.sh /ingest/kafka-etl ${BROKER_STR}`

After step 4 completes successfully, you can view the new avro
containers and avro records in HDFS.  If you want to see the persisted
records, find and download an avro container using `hadoop fs`
commands, then use the avro-tools-jar 'tojson' and 'getmeta' (this
artifact is included in most avro distributions, and can be downloaded
from avro.apache.org) commands to inspect the avro conrtainer.
