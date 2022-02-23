package com.gigamonkeys.boggle;

class Main {
  public static void main(String[] args) {
    var b = new Boggle();
    new UI(b.randomGrid()).run();
  }
}
