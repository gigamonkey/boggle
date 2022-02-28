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
  private List<List<Point>> currentPossibilities = new ArrayList<>();
  private boolean afterQ = false;
  private StringBuilder currentWord = new StringBuilder();

  Keyboard(Boggle boggle) {
    this.boggle = boggle;
    reset();
  }

  public void reset() {
    currentPossibilities.clear();
    currentPossibilities.add(Collections.emptyList());
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
        if (buttons[i].getText().equalsIgnoreCase(letter)) {
          possible.add(new Point(i % 4, i / 4));
        }
      }
      updatePossibilities(possible);
      currentWord.append(text);
    }

    if (currentPossibilities.size() == 0) {
      // Maybe report not a legal word (shake the whole board or something.)
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
    // Bet there's a more elegant way of expressing this with Stream.flatMap
    var updated = new ArrayList<List<Point>>();
    for (var path : currentPossibilities) {
      for (var p : possible) {
        if (ok(p, path)) {
          var newPath = new ArrayList<>(path);
          newPath.add(p);
          updated.add(newPath);
        }
      }
    }
    currentPossibilities = updated;
    System.out.println("Current possibilites: " + currentPossibilities);
  }

  private boolean ok(Point p, List<Point> path) {
    return Words.legalMove(path, path.size() > 0 ? path.get(path.size() - 1) : null, p);
  }
}
