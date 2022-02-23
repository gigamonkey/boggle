package com.gigamonkeys.boggle;

import java.awt.Point;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class UI {

  public static final int GAME_IN_MILLIS = 3 * 60 * 1000;

  public final static int WIDTH = 400;
  public final static int BUTTON_SIZE = 55;
  public final static int HEIGHT = (BUTTON_SIZE * 4) + 150;
  public final static int WITH_GAP = BUTTON_SIZE + 5;
  public final static int MARGIN = 20;
  public final static int X_OFFSET = (WIDTH - 4 * WITH_GAP) / 2;

  private Boggle boggle;
  private Game game;


  private JFrame frame;
  private JButton[] letterButtons;
  private JButton submit = new JButton("Submit");
  private JLabel message = new JLabel("", SwingConstants.LEFT);
  private JLabel scoreboard = new JLabel("Score: 0", SwingConstants.RIGHT);
  private JLabel clock = new JLabel("00:00", SwingConstants.LEFT);
  private long end = 0;

  UI(Boggle boggle) {
    this.boggle = boggle;
    this.game = new Game(boggle);
    this.letterButtons = new JButton[16];
    this.frame = new JFrame("Boggle");
    setupFrame();
    addDice();
    addStart();
    addSubmit();
    addScoreboard();
    addClock();
    addNotAWord();
    resetDice(false);
    frame.repaint();
  }

  public void run() {}

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
      b.setEnabled(false);
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

  private void addSubmit() {
    int x = X_OFFSET;
    int y = MARGIN + (WITH_GAP * 4) + 2;
    int w = (WITH_GAP * 4);
    submit.setBounds(x, y, w, 30);
    submit.addActionListener(new SubmitListener());
    submit.setEnabled(false);
    frame.add(submit);
  }

  private void addScoreboard() {
    scoreboard.setBounds(fromRight(100 + MARGIN/2), MARGIN/2, 100, 20);
    frame.add(scoreboard);
  }

  private void addClock() {
    clock.setBounds(MARGIN/2, MARGIN/2, 50, 20);
    frame.add(clock);
  }

  private void addNotAWord() {
    message.setBounds(MARGIN/2, fromBottom(MARGIN/2 + 20), 150, 20);
    message.setForeground(Color.red);
    frame.add(message);
  }

  void newGame() {
    this.game = new Game(boggle);
    resetDice(true);
    message.setText("");
    updateScore(0);
    end = System.currentTimeMillis() + GAME_IN_MILLIS;
    updateClock();
    startTimer();
  }

  private void updateScore(int score) {
    scoreboard.setText("Score: " + score);
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


  private void showMessage(String msg) {
    message.setText(msg);
    var t = new Timer(2000, new ClearMessage());
    t.setRepeats(false);
    t.start();
  }

  private void startTimer() {
    new Timer(1000, new Clock()).start();
  }

  private void resetDice(boolean enable) {
    var labels = boggle.facesShowing();
    for (var i = 0; i < letterButtons.length; i++) {
      letterButtons[i].setText(labels[i]);
      letterButtons[i].setEnabled(enable);
    }
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

  private class LetterPressListener implements ActionListener {

    private Point p;

    LetterPressListener(Point p) {
      this.p = p;
    }

    public void actionPerformed(ActionEvent e) {
      if (game.legal(p)) {
        var b = (JButton)e.getSource();
        game.addToWord(b.getText(), p);
      }
    }
  }


  private class SubmitListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      var w = game.getWord();
      if (w.length() > 0) {
        if (game.wordUsed(w)) {
          showMessage("“" + w + "” already used.");
        } else if (boggle.isWord(w)) {
          updateScore(game.scoreWord(w));
        } else {
          showMessage("“" + w + "” not in word list.");
        }
        game.clearWord();
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
  }

  private class ClearMessage implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      message.setText("");
    }
  }

}
