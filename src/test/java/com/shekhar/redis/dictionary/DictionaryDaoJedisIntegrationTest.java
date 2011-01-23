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
import org.junit.Test;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;

public class DictionaryDaoJedisIntegrationTest {

    private RedisTemplate<String, String> template;
    private DictionaryDao dao;

    @Before
    public void setUp() throws Exception {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setUsePool(true);
        factory.setPort(6379);
        factory.setHostName("localhost");
        factory.afterPropertiesSet();
        template = new RedisTemplate<String, String>(factory);
        template.afterPropertiesSet();
        dao = new DictionaryDao(template);
    }

    @After
    public void tearDown() throws Exception {
        template.getConnectionFactory().getConnection().flushDb();
        template.getConnectionFactory().getConnection().close();
    }

    @Test
    public void shouldAddWordWithItsMeaningToDictionary() {
        Long index = dao.addWordWithItsMeaningToDictionary("lollop",
                "To move forward with a bounding, drooping motion.");
        assertThat(index, is(notNullValue()));
        assertThat(index, is(equalTo(1L)));
    }

    @Test
    public void shouldAddMeaningToAWordIfItExists() {
        Long index = dao.addWordWithItsMeaningToDictionary("lollop",
                "To move forward with a bounding, drooping motion.");
        assertThat(index, is(notNullValue()));
        assertThat(index, is(equalTo(1L)));
        index = dao.addWordWithItsMeaningToDictionary("lollop",
                "To hang loosely; droop; dangle.");
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
        List<String> allMeaningsForLollop = dao.getAllTheMeaningsForAWord("lollop");
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
    public void shouldGetCountOfAllUniqueWordsInTheDictionary() throws Exception {
        setupTwoWords();
        int countOfAllUniqueWords = dao.countOfAllUniqueWords();
        assertThat(countOfAllUniqueWords, is(equalTo(2)));
    }

    @Test
    public void shouldGiveMeARandomWord() throws Exception {
        setupTwoWords();
        WordMeaningPair wordMeaningPair = dao.randomWord();
        assertThat(wordMeaningPair, is(notNullValue()));
        assertThat(wordMeaningPair.getWord(), is(notNullValue()));
        assertThat(!wordMeaningPair.getMeanings().isEmpty(), is(true));
    }

    private void setupTwoWords() {
        setupOneWord();
        dao.addWordWithItsMeaningToDictionary("fain",
                "Content; willing.");
        dao.addWordWithItsMeaningToDictionary("fain", "Archaic: Constrained; obliged.");
    }

    private void setupOneWord() {
        dao.addWordWithItsMeaningToDictionary("lollop",
                "To move forward with a bounding, drooping motion.");
        dao.addWordWithItsMeaningToDictionary("lollop",
                "To hang loosely; droop; dangle.");
    }
}
