package com.shekhar.redis.dictionary;

import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;

public class DictionaryDaoJedisIntegrationTest extends
		AbstractDictionaryDaoIntegrationTest {

	protected JedisConnectionFactory getConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setUsePool(true);
		factory.setPort(6379);
		factory.setHostName("localhost");
		factory.afterPropertiesSet();
		return factory;
	}
	
	protected RedisTemplate<String, String> getRedisTemplate() {
		return new RedisTemplate<String, String>(getConnectionFactory());
	}
}
