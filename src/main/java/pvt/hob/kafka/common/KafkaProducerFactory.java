package pvt.hob.kafka.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import pvt.hob.kafka.pojo.UserSerializer;

@Configuration
public class KafkaProducerFactory {

	@Autowired
	private GenericKafkaProducerConfig<UserSerializer> userKafka;
	
	@Bean
	public KafkaTemplate<String, UserSerializer> createUserBean() {
		return userKafka.kafkaTemplate();
	} 
}
