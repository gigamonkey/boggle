package com.gigamonkeys.boggle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manage the list of legal words and track guessed words, determining
 * if they are valid words and have not been guessed already.
 */
class Words {

  // Wordlist combined from:
  // https://raw.githubusercontent.com/benhoyt/boggle/master/word-list.txt
  // https://www.wordgamedictionary.com/twl06/download/twl06.txt
  // https://www.luke-g.com/boggle/
  private static final Set<String> words;

  static {
    var resource = Boggle.class.getResourceAsStream("word-list.txt");
    var r = new BufferedReader(new InputStreamReader(resource));
    words = r.lines().map(w -> w.toLowerCase()).collect(Collectors.toCollection(HashSet::new));
    System.out.println(words.size() + " words loaded.");
  }

  private Set<String> usedWords = new HashSet<>();

  public boolean isWord(String word) {
    return word.length() >= 3 && words.contains(word);
  }

  public void use(String word) {
    usedWords.add(word);
  }

  public boolean alreadyUsed(String word) {
    return usedWords.contains(word);
  }

  public Stream<String> all() {
    return words.stream();
  }

  public void reset() {
    usedWords.clear();
  }
}
