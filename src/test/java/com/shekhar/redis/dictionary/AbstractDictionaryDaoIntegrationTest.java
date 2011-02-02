package com.shekhar.redis.dictionary;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.keyvalue.redis.connection.RedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;

public abstract class AbstractDictionaryDaoIntegrationTest {

	private RedisTemplate<String, String> template;
	private DictionaryDao dao;

	public AbstractDictionaryDaoIntegrationTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.template = getRedisTemplate();
		this.template.afterPropertiesSet();
		dao = new DictionaryDao(template);
	}

	protected abstract RedisTemplate<String, String> getRedisTemplate();

	protected abstract RedisConnectionFactory getConnectionFactory();

	@After
	public void tearDown() throws Exception {
		template.getConnectionFactory().getConnection().flushDb();
		template.getConnectionFactory().getConnection().close();
	}

	@Test
	public void shouldAddWordWithItsMeaningToDictionary() {
		Long index = dao.addWordWithItsMeaningToDictionary("lollop",
				"To move forward with a bounding, drooping motion.", "verb");
		assertThat(index, is(notNullValue()));
		assertThat(index, is(equalTo(1L)));
		List<String> allMeanings = dao.getAllTheMeaningsForAWord("lollop");
		System.out.println(allMeanings);
	}

	@Test
	public void shouldAddMeaningToAWordIfItExists() {
		Long index = dao.addWordWithItsMeaningToDictionary("lollop",
				"To move forward with a bounding, drooping motion.", "verb");
		assertThat(index, is(notNullValue()));
		assertThat(index, is(equalTo(1L)));
		index = dao.addWordWithItsMeaningToDictionary("lollop",
				"To hang loosely; droop; dangle.", "verb");
		assertThat(index, is(equalTo(2L)));
	}

	@Test
	public void shouldGetAllTheMeaningForAWord() {
		setupOneWord();
		List<String> allMeanings = dao.getAllTheMeaningsForAWord("lollop");
		assertThat(allMeanings.size(), is(equalTo(2)));
		assertThat(
				allMeanings,
				hasItems("To move forward with a bounding, drooping motion.",
						"To hang loosely; droop; dangle."));
	}

	@Test
	public void shouldDeleteAWordFromDictionary() throws Exception {
		setupOneWord();
		dao.removeWord("lollop");
		List<String> allMeanings = dao.getAllTheMeaningsForAWord("lollop");
		assertThat(allMeanings.size(), is(equalTo(0)));
	}

	@Test
	public void shouldDeleteMultipleWordsFromDictionary() {
		setupTwoWords();
		dao.removeWords("fain", "lollop");
		List<String> allMeaningsForLollop = dao
				.getAllTheMeaningsForAWord("lollop");
		List<String> allMeaningsForFain = dao.getAllTheMeaningsForAWord("fain");
		assertThat(allMeaningsForLollop.size(), is(equalTo(0)));
		assertThat(allMeaningsForFain.size(), is(equalTo(0)));
	}

	@Test
	public void shouldGetAllUniqueWordsInTheDictionary() throws Exception {
		setupTwoWords();
		Set<String> allUniqueWords = dao.allUniqueWordsInDictionary();
		assertThat(allUniqueWords.size(), is(equalTo(2)));
		assertThat(allUniqueWords, hasItems("lollop", "fain"));
	}

	@Test
	public void shouldGetCountOfAllUniqueWordsInTheDictionary()
			throws Exception {
		setupTwoWords();
		int countOfAllUniqueWords = dao.countOfAllUniqueWords();
		assertThat(countOfAllUniqueWords, is(equalTo(2)));
	}

	@Ignore
	public void shouldGiveMeARandomWord() throws Exception {
		setupTwoWords();
		WordMeaningPair wordMeaningPair = dao.randomWord();
		assertThat(wordMeaningPair, is(notNullValue()));
		assertThat(wordMeaningPair.getWord(), is(notNullValue()));
		assertThat(!wordMeaningPair.getMeanings().isEmpty(), is(true));
	}

	private void setupTwoWords() {
		setupOneWord();
		dao.addWordWithItsMeaningToDictionary("fain", "Content; willing.",
				"adjective");
		dao.addWordWithItsMeaningToDictionary("fain",
				"Archaic: Constrained; obliged.", "adjective");
	}

	private void setupOneWord() {
		dao.addWordWithItsMeaningToDictionary("lollop",
				"To move forward with a bounding, drooping motion.", "verb");
		dao.addWordWithItsMeaningToDictionary("lollop",
				"To hang loosely; droop; dangle.", "verb");
	}

}