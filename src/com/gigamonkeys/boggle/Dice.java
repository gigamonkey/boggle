package com.gigamonkeys.boggle;

import static com.gigamonkeys.boggle.Util.shuffledList;

import java.util.*;

class Dice {

  private final static Random r = new Random();

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

  List<String> faces() {
    return shuffledList(MODERN).stream().map(s -> s.split("", 6)[r.nextInt(6)]).toList();
  }

}
