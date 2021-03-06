package pvt.hob.kafka;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

	// bootstrap.server - list of host and port pairs for initial conn to kafka
	// cluster
	// key.serializer - serializer class of the key which is used to impl the
	// Serializer intfc
	// value.serializer - class that implements the Serializer intfc

	private static final String bootStrapServer = "192.168.0.104:9092", topicName = "TutorialTopic";

	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class.getName());

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

		Scanner sc = new Scanner(System.in);
		boolean runLoop = Boolean.TRUE;
		String response = null;
		ProducerRecord<String, String> producerRecord = null;
		int i = 0;
		while (runLoop) {
			System.out.print( "id_" + Integer.toString(i) + ">");
			response = sc.nextLine();
			try {
				if (-1 == Integer.parseInt(response))
					runLoop = Boolean.FALSE;
			} catch (NumberFormatException ex) {
				producerRecord = new ProducerRecord<String, String>(topicName, "id_" + Integer.toString(i), response);
				try {
					producer.send(producerRecord, new Callback() {
						@Override
						public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
							if (null == exception) {
								LOGGER.info(String.format(
										"Successfully received the details as: \n Topic: %s \nPartition: %s \nOffset : %s \nTimestamp : %s",
										recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
										recordMetadata.timestamp()));
							} else {
								LOGGER.info("Cannot produce, Error : ", exception.getLocalizedMessage());
							}
						}
					}).get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				producer.flush();
				i++;
			}
		}
		producer.close();
		sc.close();
	}
}
