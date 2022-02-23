package com.gigamonkeys.boggle;

import java.awt.Point;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class UI {

  public final static int WIDTH = 400;
  public final static int BUTTON_SIZE = 55;
  public final static int HEIGHT = (BUTTON_SIZE * 4) + 150;
  public final static int WITH_GAP = BUTTON_SIZE + 5;
  public final static int MARGIN = 20;
  public final static int X_OFFSET = (WIDTH - 4 * WITH_GAP) / 2;

  private Boggle boggle;
  private JFrame frame;
  private JButton[] letterButtons;
  private JLabel scoreboard = null;
  private int score = 0;

  private Set<Point> used = new HashSet<Point>();
  private Point lastPress = null;
  private StringBuilder currentWord = new StringBuilder();

  UI(Boggle boggle) {
    this.boggle = boggle;
    this.letterButtons = new JButton[16];
    this.frame = new JFrame("Boggle");
  }

  public void run() {
    setupFrame();
    addDice();
    addStart();
    addEnter();
    addScoreboard();
    frame.repaint();
  }

  private void setupFrame() {
    frame.setSize(WIDTH, HEIGHT);
    frame.setLayout(null);
    frame.setVisible(true);
  }

  private void addDice() {
    for (var i = 0; i < 16; i++) {
      var x = i % 4;
      var y = i / 4;
      var b = newDie("", x, y);
      b.addActionListener(new LetterPressListener(new Point(x, y)));
      frame.add(b);
      letterButtons[i] = b;
    }
  }

  private void addStart() {
    JButton b = new JButton("New Game!");
    b.setBounds(fromRight(150 + MARGIN/2), fromBottom(30 + MARGIN/2), 150, 30);
    b.addActionListener(new NewGameListener());
    frame.add(b);
  }

  private void addEnter() {
    JButton b = new JButton("Submit");
    int x = X_OFFSET;
    int y = MARGIN + (WITH_GAP * 4) + 2;
    int w = (WITH_GAP * 4);
    b.setBounds(x, y, w, 30);
    b.addActionListener(new SubmitListener());
    frame.add(b);
  }

  private void addScoreboard() {
    scoreboard = new JLabel("0", SwingConstants.RIGHT);
    scoreboard.setBounds(fromRight(50 + MARGIN/2), MARGIN/2, 50, 20);
    frame.add(scoreboard);
  }

  private void newGame() {
    resetDice();
    score = 0;
    updateScore();
  }

  private void updateScore() {
    scoreboard.setText("" + score);
  }


  private void resetDice() {
    var labels = boggle.showing();
    for (var i = 0; i < letterButtons.length; i++) {
      letterButtons[i].setText(labels[i]);
    }
  }

  private String getWord() {
    return currentWord.toString().toLowerCase();
  }

  private void clearWord() {
    currentWord.delete(0, currentWord.length());
    lastPress = null;
    used.clear();
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

  private boolean legal(Point p) {
    return !used.contains(p) && (lastPress == null || adjacent(lastPress, p));
  }

  private boolean adjacent(Point p1, Point p2) {
    return Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1;
  }

  private class LetterPressListener implements ActionListener {

    private Point p;

    LetterPressListener(Point p) {
      this.p = p;
    }

    public void actionPerformed(ActionEvent e) {
      if (legal(p)) {
        var b = (JButton)e.getSource();
        var text = b.getText();
        currentWord.append(text);
        System.out.println("Got " + text + " at " + p);
        lastPress = p;
        used.add(p);
      } else {
        System.out.println(p + " already used or not adjacent to " + lastPress);
      }
    }
  }


  private class SubmitListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      var w = getWord();
      if (w.length() > 0) {
        if (boggle.isWord(w)) {
          score += boggle.points(w);
          updateScore();
        } else {
          System.out.println(w + " not in word list.");
        }
        clearWord();
      }
    }
  }

  private class NewGameListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      newGame();
    }
  }
}
