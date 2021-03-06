package pvt.hob.kafka.custom;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import pvt.hob.kafka.pojo.User;

@Component
public class KafkaUserProducer {
	@Autowired
	private Logger logger;
	@Autowired
	private KafkaTemplate<String, User> kafkaTemplate;

	@Autowired
	KafkaUserProducer(KafkaTemplate<String, User> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	void sendMessage(String topicName, User user) {
		kafkaTemplate.send(topicName, user);
	}

	void sendMessageWithCallback(String topic, User message) {
		ListenableFuture<SendResult<String, User>> future = kafkaTemplate.send(topic, message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, User>>() {
			@Override
			public void onSuccess(SendResult<String, User> result) {
				logger.info("Message [{}] delivered with offset {}", message, result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.warn("Unable to deliver message [{}]. {}", message, ex.getMessage());
			}
		});
	}
}
