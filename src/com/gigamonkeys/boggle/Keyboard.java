package com.gigamonkeys.boggle;

import java.awt.Point;
import java.util.ArrayList;
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
    reset();
  }

  public void reset() {
    currentPossibilities = List.of(Collections.emptyList());
    currentWord.delete(0, currentWord.length());
  }

  public void letter(String letter, JButton[] buttons) {
    var text = letterToText(letter);
    if (text != null) {
      currentPossibilities = updatedPossibilities(possibleButtons(text, buttons));
      currentWord.append(text);
    }
  }

  public void enter() {
    if (!currentPossibilities.isEmpty()) {
      System.out.println("Can process word: " + currentWord.toString());
      this.boggle.submitThisWord(currentWord.toString());
    } else {
      System.out.println("No possible paths to: " + currentWord.toString());
    }
    reset();
  }

  private List<Point> possibleButtons(String text, JButton[] buttons) {
    return IntStream
      .range(0, buttons.length)
      .filter(i -> buttons[i].getText().equalsIgnoreCase(text))
      .mapToObj(i -> new Point(i % 4, i / 4))
      .toList();
  }

  private List<List<Point>> updatedPossibilities(List<Point> possible) {
    return currentPossibilities
      .stream()
      .flatMap(path -> possible.stream().filter(p -> ok(p, path)).map(p -> appending(path, p)))
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
