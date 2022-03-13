package com.gigamonkeys.boggle;

import static com.gigamonkeys.boggle.Util.shuffledList;

import java.util.List;
import java.util.Random;

/**
 * Provide random legal lists of dice faces by randomly positioning
 * the dice and then randomly select a face for each die.
 */
class Dice {

  private static final Random r = new Random();

  // From http://www.bananagrammer.com/2013/10/the-boggle-cube-redesign-and-its-effect.html
  private static final String[] CLASSIC = {
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

  public static final String[] MODERN = {
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

  List<String> faces() {
    return shuffledList(MODERN).stream().map(s -> s.split("", 6)[r.nextInt(6)]).toList();
  }
}
