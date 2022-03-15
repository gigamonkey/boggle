package com.gigamonkeys.boggle;

import java.util.*;
import java.util.stream.*;

/**
 * Given a set of dice and a word list find all the words (and their score).
 */
class Solver {

  private Keyboard keyboard;
  private Words words;

  public Solver(List<String> faces, Words words) {
    this.keyboard = new Keyboard(faces);
    this.words = words;
  }

  public Stream<String> legalWords() {
    System.out.println("Finding legal words.");
    return words.words().filter(w -> w.length() > 0 && legal(w));
  }

  private boolean legal(String word) {
    keyboard.resetWord();
    for (int i = 0; i < word.length(); i++) {
      keyboard.letterTyped(word.substring(i, i + 1));
    }
    return keyboard.stillPossible();
  }
}
