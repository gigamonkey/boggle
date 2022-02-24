package com.gigamonkeys.boggle;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

class Boggle {

  private final static Random r = new Random();

  // From https://www.hasbro.com/common/instruct/boggle.pdf
  private final static int[] scores = {1, 1, 2, 3, 5, 11};

  // From http://www.bananagrammer.com/2013/10/the-boggle-cube-redesign-and-its-effect.html

  private final static String[] CLASSIC = {
    "AACIOT", "ABILTY", "ABJMOQu", "ACDEMP",
    "ACELRS", "ADENVZ", "AHMORS", "BIFORX",
    "DENOSW", "DKNOTU", "EEFHIY", "EGKLUY",
    "EGINTV", "EHINPS", "ELPSTU", "GILRUW",
  };

  public final static String[] MODERN = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNUQu", "HLNNRZ",
  };

  // Wordlist from https://raw.githubusercontent.com/benhoyt/boggle/master/word-list.txt
  private final static Set<String> words = new HashSet<String>();
  static {
    try {
      var resource = Boggle.class.getResourceAsStream("word-list.txt");
      var reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        words.add(line);
      }
      System.out.println(words.size() + " words loaded.");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  boolean isWord(String word) {
    return word.length() >= 3 && words.contains(word);
  }

  List<String> faces() {
    return Arrays.asList(MODERN).stream().map(s -> s.split("", 6)[r.nextInt(6)]).toList();
  }


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

  public static void main(String[] args) {
    new UI();
  }
}
