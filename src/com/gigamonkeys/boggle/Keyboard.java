package com.gigamonkeys.boggle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.JButton;

/*
 * Handle key events and determine whether they could have been
 * entered via button presses.
 */
class Keyboard {

  private final Boggle boggle;
  private List<List<Point>> currentPossibilities;
  private boolean afterQ = false;
  private StringBuilder currentWord = new StringBuilder();

  Keyboard(Boggle boggle) {
    this.boggle = boggle;
    currentPossibilities = List.of(Collections.emptyList());
  }

  public void reset() {
    currentPossibilities = List.of(Collections.emptyList());
    currentWord.delete(0, currentWord.length());
    boggle.resetLetterButtons();
  }

  public void letterTyped(String letter, JButton[] buttons) {
    var text = letterToText(letter);
    if (text != null) {
      var possible = possibleButtons(text, buttons);
      currentPossibilities = updatedPossibilities(possible);
      currentWord.append(text);
      highlightButtons(buttons);
    }
  }

  public void letterPressed(Point p, JButton[] buttons) {
    var i = p.y * 4 + p.x;
    currentPossibilities = updatedPossibilities(new int[] { i });
    currentWord.append(buttons[i].getText());
    highlightButtons(buttons);
  }

  public void enter() {
    if (!currentPossibilities.isEmpty()) {
      System.out.println("Can process word: " + getWord());
      this.boggle.submitThisWord(getWord());
    } else {
      System.out.println("No possible paths to: " + getWord());
    }
    reset();
  }

  String getWord() {
    return currentWord.toString().toLowerCase();
  }

  private void showPossibilities() {
    System.out.println("Possibilities:");
    for (var p : currentPossibilities) {
      System.out.println("  " + p);
    }
  }

  private void highlightButtons(JButton[] buttons) {
    showPossibilities();
    boggle.resetLetterButtons();
    for (var possibility : currentPossibilities) {
      for (var p : possibility) {
        boggle.lowlightLetterButton(buttons[p.y * 4 + p.x]);
      }
    }
  }

  private int[] possibleButtons(String text, JButton[] buttons) {
    return IntStream
      .range(0, buttons.length)
      .filter(i -> buttons[i].getText().equalsIgnoreCase(text))
      .toArray();
  }

  private List<List<Point>> updatedPossibilities(int[] possible) {
    return currentPossibilities
      .stream()
      .flatMap(path ->
        Arrays
          .stream(possible)
          .mapToObj(i -> new Point(i % 4, i / 4))
          .filter(p -> ok(p, path))
          .map(p -> appending(path, p))
      )
      .toList();
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
    return Words.legalMove(path, path.size() > 0 ? path.get(path.size() - 1) : null, p);
  }
}
