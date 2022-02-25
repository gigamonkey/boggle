package com.gigamonkeys.boggle;

import java.awt.*;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class UI {

  public static final int GAME_IN_MILLIS = 3 * 60 * 1000;

  public final static int WIDTH = 400;
  public final static int BUTTONS_SIZE = 235;
  public final static int MARGIN = 20;
  public final static int HEIGHT = BUTTONS_SIZE + 150;
  public final static int X_OFFSET = (WIDTH - BUTTONS_SIZE) / 2;

  private Boggle game;

  private JFrame frame;
  private JButton[] letterButtons;
  private JButton submit = new JButton("Submit");
  private JLabel message = new JLabel("", SwingConstants.LEFT);
  private JLabel scoreboard = new JLabel("Score: 0", SwingConstants.RIGHT);
  private JLabel clock = new JLabel("00:00", SwingConstants.LEFT);
  private long end = 0;

  UI() {
    this.game = new Boggle();
    this.letterButtons = new JButton[16];
    this.frame = new JFrame("Boggle");
    setupFrame();
    addDice();
    addStart();
    addSubmit();
    addInfo();
    addMessage();
    resetDice(false);
    frame.repaint();
  }

  private void setupFrame() {
    frame.setSize(WIDTH, HEIGHT);
    frame.setLayout(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void addDice() {

    JPanel panel = new JPanel(new GridLayout(4,4));
    panel.setBounds(X_OFFSET, MARGIN, BUTTONS_SIZE, BUTTONS_SIZE);

    for (var i = 0; i < 16; i++) {
      var x = i % 4;
      var y = i / 4;

      final var p = new Point(x, y);
      final var b = new JButton("");
      b.addActionListener(e -> dieClicked(p, b.getText()));
      b.setEnabled(false);
      panel.add(b);
      letterButtons[i] = b;
    }
    frame.add(panel);
  }

  private void addStart() {
    JButton b = new JButton("New Game!");
    b.setBounds(fromRight(150 + MARGIN/2), fromBottom(30 + MARGIN/2), 150, 30);
    b.addActionListener(e -> newGame());
    frame.add(b);
  }

  private void addSubmit() {
    int x = X_OFFSET;
    int y = MARGIN + BUTTONS_SIZE + 2;
    submit.setBounds(x, y, BUTTONS_SIZE, 30);
    submit.addActionListener(e -> submitWord());
    submit.setEnabled(false);
    frame.add(submit);
  }

  private void addInfo() {
    addScoreboard();
    addClock();
  }

  private void addScoreboard() {
    scoreboard.setBounds(fromRight(100 + MARGIN/2), MARGIN/2, 100, 20);
    frame.add(scoreboard);
  }

  private void addClock() {
    clock.setBounds(MARGIN/2, MARGIN/2, 50, 20);
    frame.add(clock);
  }

  private void addMessage() {
    message.setBounds(MARGIN/2, fromBottom(MARGIN/2 + 20), 150, 20);
    message.setForeground(Color.red);
    frame.add(message);
  }


  private void updateScore(int score) {
    scoreboard.setText("Score: " + score);
  }

  void newGame() {
    game = new Boggle();
    resetDice(true);
    message.setText("");
    updateScore(0);
    end = System.currentTimeMillis() + GAME_IN_MILLIS;
    updateClock();
    startTimer();
  }

  private void dieClicked(Point p, String text) {
    if (game.legal(p)) {
      game.addToWord(text, p);
    }
  }

  private void showMessage(String msg, Color color) {
    message.setForeground(color);
    message.setText(msg);
    var t = new Timer(2000, e -> message.setText(""));
    t.setRepeats(false);
    t.start();
  }

  private void startTimer() {
    new Timer(1000, e -> updateClock()).start();
  }

  private void updateClock() {
    var s = Math.max(0, (end - System.currentTimeMillis()) / 1000);
    var minutes = s / 60;
    var seconds = s % 60;
    clock.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : "" + seconds));
    if (s == 0) {
      game.done();
    }
    submit.setEnabled(!game.over());
  }

  private void resetDice(boolean enable) {
    var labels = game.faces();
    for (var i = 0; i < letterButtons.length; i++) {
      letterButtons[i].setText(labels.get(i));
      letterButtons[i].setEnabled(enable);
    }
  }

  void submitWord() {
    var w = game.getWord();
    if (w.length() > 0) {
      if (game.wordUsed(w)) {
        showMessage("“" + w + "” already used.", Color.red);
      } else if (game.isWord(w)) {
        showMessage("“" + w + "” is good!", Color.blue);
        updateScore(game.scoreWord(w));
      } else {
        showMessage("“" + w + "” not in word list.", Color.red);
      }
      game.clearWord();
    }
  }

  private int fromBottom(int p) {
    return (int)(frame.getRootPane().getSize().getHeight() - p);
  }

  private int fromRight(int p) {
    return (int)(frame.getRootPane().getSize().getWidth() - p);
  }

}
