package com.gigamonkeys.boggle;

import java.util.*;

class Boggle {

  private final static Random r = new Random();

  // From https://www.hasbro.com/common/instruct/boggle.pdf
  private static int[] scores = {1, 1, 2, 3, 5, 11};

  private Set<String> words = new HashSet<String>();

  private Die[] dice = Die.dice(Die.MODERN);


  Boggle(/*String wordsFile*/) {
    // Open file and add words to words set.
    //words.add("food");
    //words.add("computer");
  }

  boolean isWord(String word) {
    return words.contains(word);
  }

  int points(String word) {
    return scores[Math.min(word.length(), 8) - 3];
  }

  Die[][] randomGrid() {
    final int size = (int)Math.sqrt(dice.length);
    final Die[] shuffled = shuffledDice();

    Die[][] grid = new Die[size][size];
    for (var i = 0; i < grid.length; i++) {
      for (var j = 0; j < grid[i].length; j++) {
        grid[i][j] = shuffled[i * 4 + j];
        System.out.println(grid[i][j]);
      }
      System.out.println();
    }
    return grid;
  }

  private Die[] shuffledDice() {
    List<Die> list = Arrays.asList(dice);
    Collections.shuffle(list);
    Die[] shuffled = new Die[dice.length];
    list.toArray(shuffled);
    return shuffled;
  }
}
