package com.gigamonkeys.boggle;

import java.util.*;

class Die {

  private final static Random r = new Random();

  // From http://www.bananagrammer.com/2013/10/the-boggle-cube-redesign-and-its-effect.html
  public final static String[] CLASSIC = {
    "AACIOT",
    "ABILTY",
    "ABJMOQu",
    "ACDEMP",
    "ACELRS",
    "ADENVZ",
    "AHMORS",
    "BIFORX",
    "DENOSW",
    "DKNOTU",
    "EEFHIY",
    "EGKLUY",
    "EGINTV",
    "EHINPS",
    "ELPSTU",
    "GILRUW",
  };

  public final static String[] MODERN = {
    "AAEEGN",
    "ABBJOO",
    "ACHOPS",
    "AFFKPS",
    "AOOTTW",
    "CIMOTU",
    "DEILRX",
    "DELRVY",
    "DISTTY",
    "EEGHNW",
    "EEINSU",
    "EHRTVW",
    "EIOSST",
    "ELRTTY",
    "HIMNUQu",
    "HLNNRZ",
  };

  private final String[] faces;

  static Die[] dice(String[] set) {
    var dice = new Die[set.length];
    for (var i = 0; i < set.length; i++) {
      dice[i] = new Die(set[i]);
    }
    return dice;
  }

  private Die(String faces) {
    this.faces = faces.split("", 6);
  }

  String roll() {
    return faces[r.nextInt(faces.length)];
  }

  public String toString() {
    return String.join("", faces);
  }
}
