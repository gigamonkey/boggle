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

  private int score = 0;

  List<String> faces() {
    return shuffledList(MODERN).stream().map(s -> s.split("", 6)[r.nextInt(6)]).toList();
  }

  //
  // Scoring
  //

  int scoreWord(String w) {
    score += points(w);
    return score;
  }

  int points(String word) {
    return scores[Math.min(word.length(), 8) - 3];
  }

  int getScore() {
    return score;
  }


  public static void main(String[] args) { new UI(); }
}
