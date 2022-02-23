package com.gigamonkeys.boggle;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;


class Boggle {

  private final static Random r = new Random();


  // From https://www.hasbro.com/common/instruct/boggle.pdf
  private static int[] scores = {1, 1, 2, 3, 5, 11};

  // Wordlist from https://raw.githubusercontent.com/benhoyt/boggle/master/word-list.txt
  private Set<String> words = new HashSet<String>();

  private Die[] dice = Die.dice(Die.MODERN);

  Boggle() {
    loadWords();
  }

  boolean isWord(String word) {
    return word.length() >= 3 && words.contains(word);
  }

  int points(String word) {
    System.out.println(word + " gets " + scores[Math.min(word.length(), 8) - 3] + " points.");
    return scores[Math.min(word.length(), 8) - 3];
  }

  String[] showing() {
    String[] r = new String[16];
    Die[] dice = shuffledDice();
    for (var i = 0; i < r.length; i++) {
      r[i] = dice[i].roll();
    }
    return r;
  }

  private Die[] shuffledDice() {
    List<Die> list = Arrays.asList(dice);
    Collections.shuffle(list);
    Die[] shuffled = new Die[dice.length];
    list.toArray(shuffled);
    return shuffled;
  }

  private void loadWords() {
    try {
      var resource = getClass().getResourceAsStream("word-list.txt");
      var lines = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines().toList();
      for (String line : lines) {
        words.add(line);
      }
      System.out.println(words.size() + " words loaded.");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

}
