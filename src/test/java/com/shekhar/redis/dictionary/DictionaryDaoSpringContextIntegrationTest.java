package com.shekhar.redis.dictionary;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.redis.connection.RedisConnectionFactory;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class DictionaryDaoSpringContextIntegrationTest extends
		AbstractDictionaryDaoIntegrationTest {

	@Autowired
	private JedisConnectionFactory connectionFactory;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	protected RedisConnectionFactory getConnectionFactory() {
		connectionFactory.afterPropertiesSet();
		return connectionFactory;
	}

	@Override
	protected RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

}
