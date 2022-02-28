package com.gigamonkeys.boggle;

import java.awt.*;
import java.util.*;
import java.util.List;
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
    String text = null;

    // Tiny state machine to deal with Qu.
    boolean isQ = letter.equalsIgnoreCase("q");
    if (afterQ && letter.equalsIgnoreCase("u")) {
      text = "qu";
    } else if (!isQ) {
      text = letter;
    }
    System.out.println("letter: " + letter + "; afterQ: " + afterQ + "; text: " + text);
    afterQ = isQ;

    if (text != null) {
      Set<Point> possible = new HashSet<>();
      for (var i = 0; i < buttons.length; i++) {
        if (buttons[i].getText().equalsIgnoreCase(text)) {
          possible.add(new Point(i % 4, i / 4));
        }
      }
      updatePossibilities(possible);
      currentWord.append(text);
    }

    if (currentPossibilities.size() == 0) {
      // Maybe report not a legal word (shake the whole board or something.)
      System.out.println("No longer any possibilites.");
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

  private void updatePossibilities(Set<Point> possible) {
    currentPossibilities =
      currentPossibilities.stream().flatMap(path -> possible.stream().filter(p -> ok(p, path)).map(p -> appending(path, p))).toList();
    System.out.println("Current possibilites: " + currentPossibilities.size());
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
