package com.gigamonkeys.boggle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manage the guessed words, determining if they are valid words and
 * have not been guessed already.
 */
class Words {

  // Wordlist combined from:
  // https://raw.githubusercontent.com/benhoyt/boggle/master/word-list.txt
  // https://www.wordgamedictionary.com/twl06/download/twl06.txt
  private static final Set<String> words;

  static {
    var resource = Boggle.class.getResourceAsStream("word-list.txt");
    var reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
    words =
      reader.lines().map(line -> line.toLowerCase()).collect(Collectors.toCollection(HashSet::new));
    System.out.println(words.size() + " words loaded.");
  }

  private Set<String> usedWords = new HashSet<>();

  boolean isWord(String word) {
    return word.length() >= 3 && words.contains(word);
  }

  void use(String word) {
    usedWords.add(word);
  }

  boolean alreadyUsed(String word) {
    return usedWords.contains(word);
  }

  void reset() {
    usedWords.clear();
  }
}
