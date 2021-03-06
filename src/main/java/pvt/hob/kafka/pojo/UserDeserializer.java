package pvt.hob.kafka.pojo;

import org.apache.kafka.common.serialization.Deserializer;

public class UserDeserializer implements Deserializer<User> {


	@Override
	public User deserialize(String topic, byte[] data) {
		// TODO Auto-generated method stub
		return null;
	}

}
