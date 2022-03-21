package com.gigamonkeys.boggle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Handle virtual keyboard, both key events and letter button presses,
 * to determine what words are being entered. Key presses are checked
 * to make sure they could have been entered via legal button presses.
 */
class Keyboard {

  private final List<String> faces;
  private final StringBuilder currentWord = new StringBuilder();

  private List<List<Point>> currentPossibilities = List.of(Collections.emptyList());
  private boolean afterQ = false;

  public Keyboard(List<String> faces) {
    this.faces = faces;
  }

  /**
   * The given letter was typed. Figure out if it could possibly be
   * entered via the current dice. Implements a tiny state machine to
   * deal with Qu face.
   */
  public void letterTyped(String letter) {
    boolean isQ = letter.equalsIgnoreCase("q");
    try {
      if (afterQ && letter.equalsIgnoreCase("u")) {
        update("qu", diceFor("qu"));
      } else if (!isQ) {
        update(letter, diceFor(letter));
      }
    } finally {
      afterQ = isQ;
    }
  }

  /**
   * The given text was choosen by clicking the die at Point p.
   */
  public void letterPressed(Point p, String text) {
    // We could depend on the caller to check isPressPossible before
    // calling us, perhaps.
    if (isPressPossible(p)) {
      update(text, new Point[] { p });
    }
  }

  public boolean isPressPossible(Point p) {
    return currentPossibilities.stream().anyMatch(path -> ok(path, p));
  }

  public boolean stillPossible() {
    return !currentPossibilities.isEmpty();
  }

  public void resetWord() {
    currentWord.delete(0, currentWord.length());
    currentPossibilities = List.of(Collections.emptyList());
  }

  public String getWord() {
    // This method is used for displaying the word being built so we
    // include a typed Q before it has actually been processed.
    return currentWord.toString().toLowerCase() + (afterQ ? "q" : "");
  }

  public List<List<Point>> getPossibilities() {
    return currentPossibilities;
  }

  ////////////////////////////////////////////////////////////////////
  // Internals

  private void update(String text, Point[] possibleNexts) {
    currentPossibilities = updatedPossibilities(possibleNexts);
    currentWord.append(text);
  }

  private Point[] diceFor(String text) {
    return IntStream
      .range(0, faces.size())
      .filter(i -> faces.get(i).equalsIgnoreCase(text))
      .mapToObj(i -> new Point(i % 4, i / 4))
      .toArray(Point[]::new);
  }

  private List<List<Point>> updatedPossibilities(Point[] points) {
    return currentPossibilities
      .stream()
      .flatMap(path -> Arrays.stream(points).filter(p -> ok(path, p)).map(p -> appending(path, p)))
      .toList();
  }

  private List<Point> appending(List<Point> path, Point p) {
    var newPath = new ArrayList<>(path);
    newPath.add(p);
    return newPath;
  }

  private boolean ok(List<Point> path, Point p) {
    return path.isEmpty() || (!path.contains(p) && adjacent(previous(path), p));
  }

  private Point previous(List<Point> path) {
    return path.get(path.size() - 1);
  }

  private boolean adjacent(Point p1, Point p2) {
    return Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1;
  }
}
