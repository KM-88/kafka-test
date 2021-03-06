package pvt.hob.kafka.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pvt.hob.kafka.pojo.UserDeserializer;

@Configuration
class KafkaConsumerFactory {

	@Autowired
	private GenericKafkaConsumerConfig<UserDeserializer> userKafka;
	
	@Bean
	public void createUserConsumer() {
		userKafka.kafkaListenerContainerFactory();
	}

}
