package com.shekhar.redis.dictionary;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DictionaryDao {

	private static final String ALL_UNIQUE_WORDS = "all-unique-words";
	private RedisTemplate<String, String> template;

	@Autowired
	public DictionaryDao(RedisTemplate<String, String> template) {
		this.template = template;
	}

	public Long addWordWithItsMeaningToDictionary(String word, String meaning) {
		Long index = template.getListOps().rightPush(word, meaning);
		template.getSetOps().add(ALL_UNIQUE_WORDS, word);
		return index;
	}

	public List<String> getAllTheMeaningsForAWord(String word) {
		List<String> meanings = template.getListOps().range(word, 0, -1);
		return meanings;
	}

	public void removeWord(String word) {
		template.delete(Arrays.asList(word));
	}

	public void removeWords(String... words) {
		template.delete(Arrays.asList(words));
	}

	public Set<String> allUniqueWordsInDictionary() {
		Set<String> allUniqueWords = template.getSetOps().members(
				ALL_UNIQUE_WORDS);
		return allUniqueWords;
	}

	public int countOfAllUniqueWords() {
		Set<String> allUniqueWords = template.getSetOps().members(
				ALL_UNIQUE_WORDS);
		return allUniqueWords.size();
	}

	public WordMeaningPair randomWord() {
		String randomWord = template.getSetOps().randomMember(ALL_UNIQUE_WORDS);
		List<String> meanings = template.getListOps().range(randomWord, 0, -1);
		WordMeaningPair wordMeaningPair = new WordMeaningPair(randomWord,
				meanings);
		return wordMeaningPair;
	}

}
