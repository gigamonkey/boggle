package com.gigamonkeys.boggle;

import static com.gigamonkeys.boggle.Util.shuffledList;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.Timer;

/**
 * Manage the words as they are built up.
 */
class Words {

  // Wordlist from https://raw.githubusercontent.com/benhoyt/boggle/master/word-list.txt
  private final static Set<String> words;
  static {
    var resource = Boggle.class.getResourceAsStream("word-list.txt");
    var reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
    words = reader.lines().map(line -> line.toLowerCase()).collect(Collectors.toCollection(HashSet::new));
    System.out.println(words.size() + " words loaded.");
  }

  private Set<String> usedWords = new HashSet<String>();
  private Set<Point> usedDice = new HashSet<Point>();
  private Point lastPress = null;
  private StringBuilder currentWord = new StringBuilder();

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

  void use(String w) {
    usedWords.add(w);
  }

  boolean wasUsed(String w) {
    return usedWords.contains(w);
  }

  void reset() {
    clearWord();
    usedWords.clear();
  }
}