package com.gigamonkeys.boggle;

class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    //new UI(b.randomGrid()).run();
    /*
      var d = new Die("aeiouy");
      System.out.println(d.face());

      for (var i = 0; i < 10; i++) {
      d.roll();
      System.out.println(d.face());
      }
    */

    var b = new Boggle();
    b.randomGrid();


    if (false) {
      System.out.println(b.points("cow"));
      System.out.println(b.points("fork"));
      System.out.println(b.points("about"));
      System.out.println(b.points("awhile"));
      System.out.println(b.points("unhappy"));
      System.out.println(b.points("whatever"));
      System.out.println(b.points("extremely"));
    }
  }
}
