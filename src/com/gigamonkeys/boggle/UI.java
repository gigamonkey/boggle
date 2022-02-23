package com.gigamonkeys.boggle;

import java.awt.Point;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class UI {

  public static final int MILLIS = 3 * 60 * 1000;

  public final static int WIDTH = 400;
  public final static int BUTTON_SIZE = 55;
  public final static int HEIGHT = (BUTTON_SIZE * 4) + 150;
  public final static int WITH_GAP = BUTTON_SIZE + 5;
  public final static int MARGIN = 20;
  public final static int X_OFFSET = (WIDTH - 4 * WITH_GAP) / 2;

  private Boggle boggle;
  private JFrame frame;
  private JButton[] letterButtons;
  private JButton submit = new JButton("Submit");
  private JLabel scoreboard = new JLabel("Score: 0", SwingConstants.RIGHT);
  private JLabel clock = new JLabel("00:00", SwingConstants.LEFT);
  private int score = 0;
  private long end = 0;
  private boolean gameOver = false;

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
    addClock();
    resetDice();
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
    int x = X_OFFSET;
    int y = MARGIN + (WITH_GAP * 4) + 2;
    int w = (WITH_GAP * 4);
    submit.setBounds(x, y, w, 30);
    submit.addActionListener(new SubmitListener());
    frame.add(submit);
  }

  private void addScoreboard() {
    scoreboard.setBounds(fromRight(50 + MARGIN/2), MARGIN/2, 50, 20);
    frame.add(scoreboard);
  }

  private void addClock() {
    clock.setBounds(MARGIN/2, MARGIN/2, 50, 20);
    frame.add(clock);
  }

  private void newGame() {
    resetDice();
    score = 0;
    updateScore();
    end = System.currentTimeMillis() + MILLIS;
    updateClock();
    startTimer();
  }

  private void updateScore() {
    scoreboard.setText("Score: " + score);
  }

  private void updateClock() {
    var s = Math.max(0, (end - System.currentTimeMillis()) / 1000);
    var minutes = s / 60;
    var seconds = s % 60;
    clock.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : "" + seconds));
    if (s == 0) {
      gameOver = true;
    }
    submit.setEnabled(!gameOver);
  }


  private void startTimer() {
    new Timer(1000, new Clock()).start();
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

  private class Clock implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      updateClock();
    }
  };

}
