package com.gigamonkeys.boggle;

class Score {

  // From https://www.hasbro.com/common/instruct/boggle.pdf
  private final static int[] scores = {1, 1, 2, 3, 5, 11};

  private int score = 0;

  int scoreWord(String w) {
    score += scores[Math.min(w.length(), 8) - 3];
    return score;
  }

  void reset() {
    score = 0;
  }
}
