package com.shekhar.redis.dictionary;

import java.io.File;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;

public class DictionaryServiceTest {

	private DictionaryService service;
	private JedisConnectionFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new JedisConnectionFactory();
		factory.setUsePool(true);
		factory.setPort(6379);
		factory.setHostName("localhost");
		factory.afterPropertiesSet();
		RedisTemplate<String, String> template = new RedisTemplate<String, String>(
				factory);
		template.afterPropertiesSet();
		DictionaryDao dictionaryDao = new DictionaryDao(template);
		service = new DictionaryService();
		service.dao = dictionaryDao;
	}

	@After
	public void tearDown() throws Exception {
		factory.getConnection().flushAll();
		factory.getConnection().close();
	}

	@Test
	public void shouldDumpWordMeaningsToDatabase() throws Exception {
		long startTime = System.currentTimeMillis();
		service.dumpWordMeaningsToDatabase(new File(
				"src/test/resources/word_meanings.txt"));
		Set<String> allUniqueWordsInDictionary = service.dao
				.allUniqueWordsInDictionary();
		System.out.println(allUniqueWordsInDictionary);
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken to put "
				+ allUniqueWordsInDictionary.size() + " words in redis is : "
				+ (endTime - startTime) + " ms ");
	}
}
