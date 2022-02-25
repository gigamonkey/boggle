package com.gigamonkeys.boggle;

import java.awt.*;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class UI {

  private final static boolean colorPanels = false;

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
    mainLayout();
    resetDice(false);
    frame.setVisible(true);
  }

  private void setupFrame() {
    frame.setSize(WIDTH, HEIGHT);
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void mainLayout() {
    frame.add(infoPane());
    frame.add(Box.createVerticalStrut(10));
    frame.add(wordPicker());
    frame.add(Box.createVerticalStrut(10));
    frame.add(bottomBar());
  }

  private Component infoPane() {
    Box panel = new Box(BoxLayout.X_AXIS);
    if (colorPanels) {
      panel.setOpaque(true);
      panel.setBackground(Color.green);
    }
    panel.add(Box.createHorizontalStrut(MARGIN));
    panel.add(clock);
    panel.add(Box.createHorizontalGlue());
    panel.add(scoreboard);
    panel.add(Box.createHorizontalStrut(MARGIN));
    return panel;
  }

  private Component wordPicker() {
    Box panel = new Box(BoxLayout.Y_AXIS);
    if (colorPanels) {
      panel.setOpaque(true);
      panel.setBackground(new Color(1.0f, 0.5f, 0.5f, 0.25f));
    }
    panel.add(dicePanel());
    panel.add(Box.createVerticalStrut(3));
    panel.add(submitButton());
    return panel;
  }

  private Component bottomBar() {
    Box panel = new Box(BoxLayout.X_AXIS);
    if (colorPanels) {
      panel.setOpaque(true);
      panel.setBackground(new Color(0.5f, 1.0f, 0.5f, 0.25f));
    }
    panel.add(Box.createHorizontalStrut(MARGIN));
    panel.add(messageBox());
    panel.add(Box.createHorizontalGlue());
    panel.add(startButton());
    panel.add(Box.createHorizontalStrut(MARGIN));
    panel.setPreferredSize(new Dimension(WIDTH, 20));
    return panel;
  }

  private JPanel dicePanel() {

    JPanel panel = new JPanel(new GridLayout(4,4));
    panel.setOpaque(false);

    Dimension d = new Dimension(BUTTONS_SIZE, BUTTONS_SIZE);
    panel.setPreferredSize(d);
    panel.setMaximumSize(d);

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
    return panel;
  }

  private Component submitButton() {
    Dimension d = new Dimension((int)(BUTTONS_SIZE * 0.95), 20);
    submit.setPreferredSize(d);
    submit.setMaximumSize(d);
    submit.addActionListener(e -> submitWord());
    submit.setEnabled(false);
    submit.setAlignmentX(Component.CENTER_ALIGNMENT);
    return submit;
  }

  private Component startButton() {
    JButton b = new JButton("New Game!");
    b.addActionListener(e -> newGame());
    return b;
  }

  private Component messageBox() {
    Dimension d = new Dimension(150, 20);
    message.setPreferredSize(d);
    message.setForeground(Color.red);
    return message;
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
}
