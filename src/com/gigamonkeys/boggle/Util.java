package com.gigamonkeys.boggle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Util {

  /**
   * Copy a given array and return a shuffled version as a List.
   */
  public static <T> List<T> shuffledList(T[] array) {
    List<T> list = Arrays.asList(array.clone());
    Collections.shuffle(list);
    return list;
  }

}
