package com.gigamonkeys.boggle;

import java.awt.Point;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/*
 * Keep the state for one game. A new Game object is created for each
 * game so we don't have to reset any of this state.
 */
public class Game {

  // From https://www.hasbro.com/common/instruct/boggle.pdf
  private static int[] scores = {1, 1, 2, 3, 5, 11};

  private int score = 0;
  private boolean gameOver = false;
  Set<String> usedWords = new HashSet<String>();

  private Point lastPress = null;
  private Set<Point> usedDice = new HashSet<Point>();
  private StringBuilder currentWord = new StringBuilder();

  public int getScore() {
    return score;
  }

  public boolean wordUsed(String w) {
    return usedWords.contains(w);
  }

  public int scoreWord(String w) {
    score += points(w);
    usedWords.add(w);
    return score;
  }

  public int points(String word) {
    return scores[Math.min(word.length(), 8) - 3];
  }

  public void done() {
    gameOver = true;
  }

  public boolean over() {
    return gameOver;
  }

  public String getWord() {
    return currentWord.toString().toLowerCase();
  }

  public void addToWord(String letter, Point p) {
    currentWord.append(letter);
    lastPress = p;
    usedDice.add(p);
  }

  public void clearWord() {
    currentWord.delete(0, currentWord.length());
    lastPress = null;
    usedDice.clear();
  }

  public boolean legal(Point p) {
    return !usedDice.contains(p) && (lastPress == null || adjacent(lastPress, p));
  }

  private boolean adjacent(Point p1, Point p2) {
    return Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1;
  }

}
