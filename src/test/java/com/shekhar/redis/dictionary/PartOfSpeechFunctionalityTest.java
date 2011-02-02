package com.shekhar.redis.dictionary;

import java.io.File;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;

public class PartOfSpeechFunctionalityTest {

	private DictionaryService service;
	private JedisConnectionFactory factory;
	private DictionaryDao dictionaryDao;

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
		dictionaryDao = new DictionaryDao(template);
		service = new DictionaryService();
		service.dao = dictionaryDao;
		service.dumpWordMeaningsToDatabase(new File(
				"src/test/resources/word_meanings.txt"));
	}

	@After
	public void tearDown() throws Exception {
		factory.getConnection().flushAll();
		factory.getConnection().close();
	}

	@Test
	public void datastoreShouldContainAllPartsOfSpeech() throws Exception {
		Long nouns = dictionaryDao.countOfMembersInASet("noun");
		Long adjectives = dictionaryDao.countOfMembersInASet("adjective");
		Long verbs = dictionaryDao.countOfMembersInASet("verb");
		System.out.println("Nouns : " + nouns + " , Adjectives : " + adjectives
				+ " , Verbs :" + verbs);
		Assert.assertTrue(nouns > 0);
		Assert.assertTrue(adjectives > 0);
		Assert.assertTrue(verbs > 0);
	}

	@Test
	public void findWordsThatAreBothNounAndVerb() throws Exception {
		Set<String> wordsWhichAreBothAreNounAndVerb = dictionaryDao
				.findWordWhichAre("noun", "verb");
		System.out.println(wordsWhichAreBothAreNounAndVerb);
		Assert.assertEquals(220, wordsWhichAreBothAreNounAndVerb.size());
	}

	@Test
	public void findWordsThatAreBothVerbAndNoun() throws Exception {
		Set<String> wordsWhichAreBothAreVerbAndNoun = dictionaryDao
				.findWordWhichAre("verb", "noun");
		System.out.println(wordsWhichAreBothAreVerbAndNoun);
		Assert.assertEquals(220, wordsWhichAreBothAreVerbAndNoun.size());
	}

	@Test
	public void findWordsThatAreBothNounAndAdjective() throws Exception {
		Set<String> wordsWhichAreBothAreNounAndAdjective = dictionaryDao
				.findWordWhichAre("noun", "adjective");
		System.out.println(wordsWhichAreBothAreNounAndAdjective);
		Assert.assertEquals(46, wordsWhichAreBothAreNounAndAdjective.size());
	}

	@Test
	public void findWordsThatAreBothAdjectiveAndNoun() throws Exception {
		Set<String> wordsWhichAreBothAreAdjectiveAndNoun = dictionaryDao
				.findWordWhichAre("adjective", "noun");
		System.out.println(wordsWhichAreBothAreAdjectiveAndNoun);
		Assert.assertEquals(46, wordsWhichAreBothAreAdjectiveAndNoun.size());
	}

	@Test
	public void findWordsThatAreBothNounVerbAndAdjective() throws Exception {
		Set<String> wordsWhichAreBothAreNounVerbAndAdjective = dictionaryDao
				.findWordWhichAre("noun", "verb", "adjective");
		System.out.println(wordsWhichAreBothAreNounVerbAndAdjective);
		Assert.assertEquals(3, wordsWhichAreBothAreNounVerbAndAdjective.size());
	}
}
