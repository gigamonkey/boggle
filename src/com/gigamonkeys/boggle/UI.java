package com.gigamonkeys.boggle;

import javax.swing.*;

public class UI {

  public final static int BUTTON_SIZE = 55;
  public final static int WITH_GAP = BUTTON_SIZE + 5;

  private Die[][] grid;

  UI(Die[][] grid) {
    this.grid = grid;
  }


  public void run() {
    JFrame f = new JFrame("Boggle");

    addDice(f, grid);

    f.setSize(400, 500);// 400 width and 500 height
    f.setLayout(null);// using no layout managers
    f.setVisible(true);// making the frame visible
  }

  void addDice(JFrame f, Die[][] grid) {
    for (var i = 0; i < grid.length; i++) {
      for (var j = 0; j < grid[i].length; j++) {
        f.add(newDie(grid[i][j].face(), i, j));
      }
    }
  }

  private JButton newDie(String label, int x, int y) {
    JButton b = new JButton(label);
    b.setBounds(x * WITH_GAP, y * WITH_GAP, BUTTON_SIZE, BUTTON_SIZE);
    return b;
  }


  // Start -- rolls dice, starts timer.
  // Dice -- 4x4 grid of dice buttons.
  // Enter -- submit the word typed so far.


}
