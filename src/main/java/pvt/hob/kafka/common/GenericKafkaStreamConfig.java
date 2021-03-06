package pvt.hob.kafka.common;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenericKafkaStreamConfig<T extends Serde> {

	private Class<T> clazz;

	@Value("{kafka.applicationID}")
	private String applicationID;


	@Value("{kafka.kafka.topicName}")
	private String topicName;
	
	@Autowired
	private Map<String, String> properties;

	@Bean
	public KafkaStreams consumerFactory(Class<?> clazz) {
		Properties prop = new Properties();
		prop.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationID);
		prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, properties.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
		prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, clazz.getClass());
		prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, clazz.getClass());
		prop.put(StreamsConfig.STATE_DIR_CONFIG, Paths.get(System.getenv("java.io.tmpdir")).toAbsolutePath());
		return new KafkaStreams(kafkaStreamTopology(),prop);
	}

	@Bean
	public Topology kafkaStreamTopology() {
		final StreamsBuilder streamsBuilder = new StreamsBuilder();
		streamsBuilder.stream(topicName);
		return streamsBuilder.build();
	}
}
