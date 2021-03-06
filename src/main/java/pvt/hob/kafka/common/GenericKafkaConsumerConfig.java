package pvt.hob.kafka.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class GenericKafkaConsumerConfig<T extends Deserializer> {

	private Class<T> clazz;

	@Autowired
	private Map<String, String> properties;

	@Bean
	public ConsumerFactory<String, T> consumerFactory(Class<?> clazz) {
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
		prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, clazz);
		prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, clazz);
		return new DefaultKafkaConsumerFactory<>(prop);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, T>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory(clazz));
		return factory;
	}
}
