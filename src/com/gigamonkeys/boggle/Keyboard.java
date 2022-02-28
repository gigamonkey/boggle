package com.gigamonkeys.boggle;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.JButton;

/*
 * Handle virtual keyboard, both key events and letter button presses,
 * to determine what words are being entered. Key presses are checked
 * to make sure they could have been entered via legal button presses.
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

  public void letterTyped(String letter, JButton[] buttons) {
    var text = letterToText(letter);
    if (text != null) {
      currentPossibilities = updatedPossibilities(possibleButtons(text, buttons));
      updateWord(text, buttons);
    }
  }

  public void letterPressed(Point p, JButton[] buttons) {
    var i = p.y * 4 + p.x;
    currentPossibilities = updatedPossibilities(new int[] { i });
    updateWord(buttons[i].getText(), buttons);
  }

  public void enter() {
    if (!currentPossibilities.isEmpty()) {
      this.boggle.submitWord(getWord());
    } else {
      boggle.showMessage("Can't make " + getWord(), Color.red, boggle.FLASH);
    }
    reset();
  }

  private void updateWord(String text, JButton[] buttons) {
    currentWord.append(text);
    highlightButtons(buttons);
    boggle.showMessage(getWord(), Color.black);
  }

  private void reset() {
    currentPossibilities = List.of(Collections.emptyList());
    currentWord.delete(0, currentWord.length());
    boggle.resetLetterButtons();
  }

  private String getWord() {
    return currentWord.toString().toLowerCase();
  }

  private void showPossibilities() {
    System.out.println("Possibilities:");
    for (var p : currentPossibilities) {
      System.out.println("  " + p);
    }
  }

  private void highlightButtons(JButton[] buttons) {
    boggle.resetLetterButtons();
    for (var possibility : currentPossibilities) {
      for (var i = 0; i < possibility.size(); i++) {
        var p = possibility.get(i);
        if (i == possibility.size() - 1) {
          boggle.highlightLetterButton(buttons[p.y * 4 + p.x]);
        } else {
          boggle.lowlightLetterButton(buttons[p.y * 4 + p.x]);
        }
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
    return path.isEmpty() || legalMove(path, path.get(path.size() - 1), p);
  }

  private boolean legalMove(List<Point> path, Point previous, Point p) {
    return !path.contains(p) && adjacent(previous, p);
  }

  private boolean adjacent(Point p1, Point p2) {
    return Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1;
  }
}
