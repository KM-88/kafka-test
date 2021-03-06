package pvt.hob.kafka.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class GenericKafkaProducerConfig<T extends Serializer> {

	private Class<T> clazz;

	@Autowired
	private Map<String, String> properties;

	@Bean
	public ProducerFactory<String, T> producerFactory(Class<?> clazz) {
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
		prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, clazz);
		prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, clazz);
		return new DefaultKafkaProducerFactory<>(prop);
	}

	@Bean
	public KafkaTemplate<String, T> kafkaTemplate() {
		ProducerFactory<String, T> factory = producerFactory(clazz);
		return new KafkaTemplate<>(factory);
	}
}
