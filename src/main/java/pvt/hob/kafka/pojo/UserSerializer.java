package pvt.hob.kafka.pojo;

import org.apache.kafka.common.serialization.Serializer;

public class UserSerializer implements Serializer<User> {

	@Override
	public byte[] serialize(String topic, User data) {
		// TODO Auto-generated method stub
		return null;
	}



}
