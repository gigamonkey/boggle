package com.gigamonkeys.boggle;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Handle virtual keyboard, both key events and letter button presses,
 * to determine what words are being entered. Key presses are checked
 * to make sure they could have been entered via legal button presses.
 */
class Keyboard {

  private final Boggle boggle;
  private StringBuilder currentWord = new StringBuilder();
  private List<List<Point>> currentPossibilities = List.of(Collections.emptyList());
  private boolean afterQ = false;

  Keyboard(Boggle boggle) {
    this.boggle = boggle;
  }

  /**
   * The given letter was typed (presumably on a keyoard). Figure out
   * if it could possibly be entered via the current dice.
   */
  public void letterTyped(String letter) {
    var text = letterToText(letter);
    if (text != null) {
      // Text is null when Q is typed. We will process the text once
      // we get the U.
      currentPossibilities = updatedPossibilities(boggle.diceFor(text));
      updateWord(text);
    } else {
      if (afterQ) {
        // Because we don't process the Q until we see the U we need
        // to go ahead and update the UI anyway.
        boggle.showMessage(getWord() + "q", Color.black);
      }
    }
  }

  /**
   * The given text was choosen by clicking the die at Point p.
   */
  public void letterPressed(Point p, String text) {
    if (isPressPossible(p)) {
      currentPossibilities = updatedPossibilities(new Point[] { p });
      updateWord(text);
    } else {
      boggle.illegalPress(p);
    }
  }

  public void enter() {
    if (!currentPossibilities.isEmpty()) {
      this.boggle.submitWord(getWord());
    } else {
      boggle.flashMessage("Can't make " + getWord(), Color.red);
    }
    resetWord();
  }

  private void updateWord(String text) {
    currentWord.append(text);
    boggle.highlightButtons(currentPossibilities);
    boggle.showMessage(getWord(), Color.black);
  }

  private void resetWord() {
    currentWord.delete(0, currentWord.length());
    currentPossibilities = List.of(Collections.emptyList());
    boggle.resetLetterButtons();
  }

  private String getWord() {
    return currentWord.toString().toLowerCase();
  }

  private List<List<Point>> updatedPossibilities(Point[] points) {
    return currentPossibilities
      .stream()
      .flatMap(path -> Arrays.stream(points).filter(p -> ok(p, path)).map(p -> appending(path, p)))
      .toList();
  }

  private boolean isPressPossible(Point p) {
    return currentPossibilities.stream().anyMatch(path -> ok(p, path));
  }

  private String letterToText(String letter) {
    // Tiny state machine to deal with Qu face.
    boolean isQ = letter.equalsIgnoreCase("q");
    try {
      if (afterQ && letter.equalsIgnoreCase("u")) {
        return "qu";
      } else if (!isQ) {
        return letter;
      } else {
        return null;
      }
    } finally {
      afterQ = isQ;
    }
  }

  private List<Point> appending(List<Point> path, Point p) {
    var newPath = new ArrayList<>(path);
    newPath.add(p);
    return newPath;
  }

  private boolean ok(Point p, List<Point> path) {
    return path.isEmpty() || legalMove(path, path.get(path.size() - 1), p);
  }

  private boolean legalMove(List<Point> path, Point previous, Point p) {
    return !path.contains(p) && adjacent(previous, p);
  }

  private boolean adjacent(Point p1, Point p2) {
    return Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1;
  }
}
