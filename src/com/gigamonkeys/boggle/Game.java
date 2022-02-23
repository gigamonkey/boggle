package com.gigamonkeys.boggle;

import java.awt.Point;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Game {

  private Boggle boggle;

  private int score = 0;
  private boolean gameOver = false;
  Set<String> usedWords = new HashSet<String>();

  private Point lastPress = null;
  private Set<Point> usedDice = new HashSet<Point>();
  private StringBuilder currentWord = new StringBuilder();

  Game(Boggle boggle) {
    this.boggle = boggle;
  }

  public int getScore() {
    return score;
  }

  public boolean wordUsed(String w) {
    return usedWords.contains(w);
  }

  public int scoreWord(String w) {
    score += boggle.points(w);
    usedWords.add(w);
    return score;
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
