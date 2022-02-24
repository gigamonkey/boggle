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
        words.add(line.toLowerCase());
      }
      System.out.println(words.size() + " words loaded.");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private int score = 0;
  private boolean gameOver = false;
  private Set<String> usedWords = new HashSet<String>();
  private Set<Point> usedDice = new HashSet<Point>();
  private Point lastPress = null;
  private StringBuilder currentWord = new StringBuilder();

  List<String> faces() {
    return shuffled(Arrays.asList(MODERN)).stream().map(s -> s.split("", 6)[r.nextInt(6)]).toList();
  }

  List<String> shuffled(List<String> items) {
    Collections.shuffle(items);
    return items;
  }


  //
  // Word management
  //

  boolean legal(Point p) {
    return !usedDice.contains(p) && (lastPress == null || adjacent(lastPress, p));
  }

  boolean adjacent(Point p1, Point p2) {
    return Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1;
  }

  void addToWord(String letter, Point p) {
    assert legal(p);
    currentWord.append(letter);
    lastPress = p;
    usedDice.add(p);
  }

  String getWord() {
    return currentWord.toString().toLowerCase();
  }

  void clearWord() {
    currentWord.delete(0, currentWord.length());
    lastPress = null;
    usedDice.clear();
  }

  boolean isWord(String word) {
    return word.length() >= 3 && words.contains(word);
  }

  boolean wordUsed(String w) {
    return usedWords.contains(w);
  }

  int scoreWord(String w) {
    score += points(w);
    usedWords.add(w);
    return score;
  }

  int points(String word) {
    return scores[Math.min(word.length(), 8) - 3];
  }

  int getScore() {
    return score;
  }

  void done() {
    gameOver = true;
  }

  boolean over() {
    return gameOver;
  }

  public static void main(String[] args) { new UI(); }
}
