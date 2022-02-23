package com.gigamonkeys.boggle;

class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    //new UI().run();
    var b = new Boggle("foo");
    System.out.println(b.points("cow"));
    System.out.println(b.points("fork"));
    System.out.println(b.points("about"));
    System.out.println(b.points("awhile"));
    System.out.println(b.points("unhappy"));
    System.out.println(b.points("whatever"));
    System.out.println(b.points("extremely"));
  }
}
