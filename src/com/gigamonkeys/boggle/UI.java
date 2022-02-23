package com.gigamonkeys.boggle;

import javax.swing.*;

public class UI {
  public void run() {
    JFrame f = new JFrame("Boggle");
    var b = newButton("A");
    f.add(b);// adding button in JFrame

    f.setSize(400, 500);// 400 width and 500 height
    f.setLayout(null);// using no layout managers
    f.setVisible(true);// making the frame visible
  }
  private JButton newButton(String label) {
    JButton b = new JButton(label);
    b.setBounds(130, 100, 100, 40);
    return b;
  }
}
