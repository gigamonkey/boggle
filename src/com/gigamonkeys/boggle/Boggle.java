package com.gigamonkeys.boggle;

import java.util.*;

class Boggle {

  // From https://www.hasbro.com/common/instruct/boggle.pdf
  private static int[] scores = {1, 1, 2, 3, 5, 11};

  private Set<String> words = new HashSet<String>();

  Boggle(String wordsFile) {
    // Open file and add words to words set.
    words.add("food");
    words.add("computer");
  }

  boolean isWord(String word) {
    return words.contains(word);
  }

  int points(String word) {
    return scores[Math.min(word.length(), 8) - 3];
  }
}
