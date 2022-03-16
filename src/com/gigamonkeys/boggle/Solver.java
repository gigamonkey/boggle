package com.gigamonkeys.boggle;

import java.util.*;
import java.util.stream.*;

/**
 * Given a set of dice faces and a word list find all the words that
 * can be made.
 */
class Solver {

  private Keyboard keyboard;
  private Words words;

  public Solver(List<String> faces, Words words) {
    this.keyboard = new Keyboard(faces);
    this.words = words;
  }

  public Stream<String> legalWords() {
    return words.all().filter(w -> w.length() >= 3 && legal(w));
  }

  private boolean legal(String word) {
    keyboard.resetWord();
    for (int i = 0; i < word.length(); i++) {
      keyboard.letterTyped(word.substring(i, i + 1));
      // Check after every letter because most words will fail
      // quickly and we want to bail.
      if (!keyboard.stillPossible()) {
        return false;
      }
    }
    return true;
  }
}
