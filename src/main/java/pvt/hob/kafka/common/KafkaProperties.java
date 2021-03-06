package pvt.hob.kafka.common;

import java.util.Map;
import java.util.TreeMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProperties {
	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${kafka.topicName}")
	private String topicName;
	@Value("${kafka.groupID}")
	private String groupId;

	@Bean
	public Map<String, String> defaultKafkaProperties() {
		Map<String, String> properties = new TreeMap<String, String>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return properties;
	}

}
