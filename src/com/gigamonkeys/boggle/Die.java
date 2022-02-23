package com.gigamonkeys.boggle;

import java.util.*;

class Die {

  private final static Random r = new Random();

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
