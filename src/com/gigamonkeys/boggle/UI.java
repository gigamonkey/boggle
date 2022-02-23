package com.gigamonkeys.boggle;

import java.awt.event.*;
import javax.swing.*;

public class UI {

  public final static int WIDTH = 400;
  public final static int BUTTON_SIZE = 55;
  public final static int HEIGHT = (BUTTON_SIZE * 4) + 150;
  public final static int WITH_GAP = BUTTON_SIZE + 5;
  public final static int MARGIN = 20;
  public final static int X_OFFSET = (WIDTH - 4 * WITH_GAP) / 2;

  private Die[][] grid;
  private JFrame frame;
  private JButton[] letterButtons;

  UI(Die[][] grid) {
    this.grid = grid;
    this.letterButtons = new JButton[16];
    this.frame = new JFrame("Boggle");
  }

  public void run() {
    setupFrame();
    addDice();
    addStart();
    addEnter();
    frame.repaint();
    System.out.println("frame: " + frame.getSize());
    System.out.println("rootPane:" + frame.getRootPane().getSize());

  }

  private void setupFrame() {
    frame.setSize(WIDTH, HEIGHT);
    frame.setLayout(null);
    frame.setVisible(true);
  }

  private void addDice() {
    for (var i = 0; i < grid.length; i++) {
      for (var j = 0; j < grid[i].length; j++) {
        var b = newDie(grid[i][j].face(), i, j);
        frame.add(b);
        letterButtons[i * 4 + j] = b;
      }
    }
  }

  private void addStart() {
    JButton b = new JButton("New Game!");
    b.setBounds(fromRight(150 + MARGIN/2), fromBottom(30 + MARGIN/2), 150, 30);
    b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.out.println("Here");
        }
      });
    frame.add(b);
  }

  private void addEnter() {
    JButton b = new JButton("Submit");
    int x = X_OFFSET;
    int y = MARGIN + (WITH_GAP * 4) + 2;
    int w = (WITH_GAP * 4);
    b.setBounds(x, y, w, 30);
    frame.add(b);

  }


  private JButton newDie(String label, int x, int y) {
    JButton b = new JButton(label);
    b.setBounds(X_OFFSET + (x * WITH_GAP), MARGIN + y * WITH_GAP, BUTTON_SIZE, BUTTON_SIZE);
    return b;
  }

  private int fromBottom(int p) {
    return (int)(frame.getRootPane().getSize().getHeight() - p);
  }

  private int fromRight(int p) {
    return (int)(frame.getRootPane().getSize().getWidth() - p);
  }


  // Start -- rolls dice, starts timer.
  // Dice -- 4x4 grid of dice buttons.
  // Enter -- submit the word typed so far.


}
