package com.shekhar.redis.dictionary;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.keyvalue.redis.connection.jredis.JredisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;

public class DictionaryDaoJredisIntegrationTest extends
		AbstractDictionaryDaoIntegrationTest {

	@Override
	protected JredisConnectionFactory getConnectionFactory() {
		JredisConnectionFactory factory = new JredisConnectionFactory();
		factory.setUsePool(true);
		factory.setPort(6379);
		factory.setHostName("localhost");
		factory.afterPropertiesSet();
		return factory;
	}

	protected RedisTemplate<String, String> getRedisTemplate() {
		return new RedisTemplate<String, String>(getConnectionFactory());
	}

	@Ignore
	public void shouldAddWordWithItsMeaningToDictionary() {
	}

	@Ignore
	public void shouldAddMeaningToAWordIfItExists() {
	}
}
