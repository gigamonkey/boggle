package com.gigamonkeys.boggle;

/**
 * Keep track of the current score and apply the scoring rules.
 */
class Scorekeeper {

  // From https://www.hasbro.com/common/instruct/boggle.pdf
  private static final int[] scores = { 1, 1, 2, 3, 5, 11 };

  private int score = 0;

  int scoreWord(String w) {
    score += scores[Math.min(w.length(), 8) - 3];
    return score;
  }

  void reset() {
    score = 0;
  }
}
