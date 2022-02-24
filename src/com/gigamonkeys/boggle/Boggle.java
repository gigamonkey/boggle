package com.gigamonkeys.boggle;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;


class Boggle {

  private final static Random r = new Random();

  // From http://www.bananagrammer.com/2013/10/the-boggle-cube-redesign-and-its-effect.html

  public final static String[] CLASSIC = {
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
  private static Set<String> words = new HashSet<String>();

  boolean isWord(String word) {
    return word.length() >= 3 && words.contains(word);
  }

  List<String> faces() {
    return Arrays.asList(MODERN).stream().map(s -> s.split("", 6)[r.nextInt(6)]).toList();
  }

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

}
