package com.shekhar.redis.dictionary;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService {

	@Autowired
	DictionaryDao dao;

	public void dumpWordMeaningsToDatabase(File input) throws IOException {

		@SuppressWarnings("unchecked")
		List<String> lines = (List<String>) FileUtils.readLines(input);
		for (String line : lines) {
			String[] wordAndItsMeanings = line.split("\\s");
			String partOfSpeech = partOfSpeech(wordAndItsMeanings[0].trim());
			String word = wordAndItsMeanings[1].trim();
			StringBuilder meaningBuilder = new StringBuilder();
			for (int i = 2; i < wordAndItsMeanings.length; i++) {
				meaningBuilder.append(wordAndItsMeanings[i]).append(" ");
			}
			meaningBuilder.trimToSize();
			String meaning = meaningBuilder.toString();
			dao.addWordWithItsMeaningToDictionary(word, meaning, partOfSpeech);
		}
	}

	private String partOfSpeech(String abbOfPartOfSpeech) {
		if ("a".equals(abbOfPartOfSpeech)) {
			return "adjective";
		} else if ("n".equals(abbOfPartOfSpeech)) {
			return "noun";
		} else if ("v".equals(abbOfPartOfSpeech)) {
			return "verb";
		}
		return null;

	}

}
