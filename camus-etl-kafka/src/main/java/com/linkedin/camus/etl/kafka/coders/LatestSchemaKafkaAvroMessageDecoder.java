package com.linkedin.camus.etl.kafka.coders;

import com.linkedin.camus.coders.CamusWrapper;
import kafka.message.Message;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DecoderFactory;

public class LatestSchemaKafkaAvroMessageDecoder extends KafkaAvroMessageDecoder
{

	@Override
	public CamusWrapper<Record> decode(byte[] payload)
	{
		try
		{
            Schema schema = super.registry.getLatestSchemaByTopic(super.topicName).getSchema();
            GenericDatumReader<Record> reader = new GenericDatumReader<Record>(schema);
            Record record = reader.read(null, DecoderFactory.get().binaryDecoder(payload, Message.MagicLength(),
                    payload.length - Message.MagicLength(), null));
            return new CamusWrapper<Record>(record);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}