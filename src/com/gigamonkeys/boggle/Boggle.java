package com.gigamonkeys.boggle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

class Boggle {

  public static void main(String[] args) {
    new Boggle().makeFrame();
  }

  public static final int GAME_IN_MILLIS = 3 * 60 * 1000;

  public static final int WIDTH = 400;
  public static final int BUTTONS_SIZE = 235;
  public static final int MARGIN = 20;
  public static final int HEIGHT = BUTTONS_SIZE + 150;

  private final Score score = new Score();
  private final Words words = new Words();
  private final Dice dice = new Dice();

  private final JButton[] letterButtons = new JButton[16];
  private final JButton submit = new JButton("Submit");
  private final JLabel clock = new JLabel("0:00", SwingConstants.LEFT);
  private final JLabel scoreboard = new JLabel(
    "Score: 0",
    SwingConstants.RIGHT
  );
  private final JLabel message = new JLabel("", SwingConstants.LEFT);

  private long endOfGame = System.currentTimeMillis();
  private Timer clockTimer;

  Boggle() {
    clockTimer = new Timer(1000, e -> updateClock());
    clockTimer.setInitialDelay(0);
    clockTimer.start();
  }

  private void makeFrame() {
    JFrame frame = new JFrame("Boggle");
    frame.setSize(WIDTH, HEIGHT);
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addComponents(frame);
    resetDice(false);
    frame.setVisible(true);
  }

  private void addComponents(JFrame frame) {
    frame.add(infoPane());
    frame.add(Box.createVerticalStrut(10));
    frame.add(wordPicker());
    frame.add(Box.createVerticalStrut(10));
    frame.add(bottomBar());
  }

  private Component infoPane() {
    Box panel = new Box(BoxLayout.X_AXIS);
    panel.add(Box.createHorizontalStrut(MARGIN));
    panel.add(clock);
    panel.add(Box.createHorizontalGlue());
    panel.add(scoreboard);
    panel.add(Box.createHorizontalStrut(MARGIN));
    return panel;
  }

  private Component wordPicker() {
    Box panel = new Box(BoxLayout.Y_AXIS);
    panel.add(dicePanel());
    panel.add(Box.createVerticalStrut(3));
    panel.add(submitButton());
    return panel;
  }

  private Component bottomBar() {
    Box panel = new Box(BoxLayout.X_AXIS);
    panel.add(Box.createHorizontalStrut(MARGIN));
    panel.add(messageBox());
    panel.add(Box.createHorizontalGlue());
    panel.add(startButton());
    panel.add(Box.createHorizontalStrut(MARGIN));
    panel.setPreferredSize(new Dimension(WIDTH, 20));
    return panel;
  }

  private JPanel dicePanel() {
    JPanel panel = new JPanel(new GridLayout(4, 4));
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
    Dimension d = new Dimension((int) (BUTTONS_SIZE * 0.95), 20);
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

  private void newGame() {
    score.reset();
    words.reset();
    resetDice(true);
    submit.setEnabled(true);
    message.setText("");
    updateScore(0);
    startClock();
  }

  private void gameOver() {
    submit.setEnabled(false);
  }

  private void dieClicked(Point p, String text) {
    if (words.legal(p)) {
      words.addToWord(text, p);
    }
  }

  private void showMessage(String msg, Color color) {
    message.setForeground(color);
    message.setText(msg);
    var t = new Timer(2000, e -> message.setText(""));
    t.setRepeats(false);
    t.start();
  }

  private void startClock() {
    endOfGame = System.currentTimeMillis() + GAME_IN_MILLIS;
    clockTimer.restart();
  }

  private void updateClock() {
    var s = Math.max(
      0,
      Math.round((endOfGame - System.currentTimeMillis()) / 1000.0)
    );
    var minutes = s / 60;
    var seconds = s % 60;
    clock.setText(
      minutes + ":" + (seconds < 10 ? "0" + seconds : "" + seconds)
    );
    if (s == 0) gameOver();
  }

  private void resetDice(boolean enable) {
    var labels = dice.faces();
    for (var i = 0; i < letterButtons.length; i++) {
      letterButtons[i].setText(labels.get(i));
      letterButtons[i].setEnabled(enable);
    }
  }

  void submitWord() {
    var w = words.getWord();
    if (w.length() > 0) {
      if (words.wasUsed(w)) {
        showMessage("“" + w + "” already used.", Color.red);
      } else if (words.isWord(w)) {
        showMessage("“" + w + "” is good!", Color.blue);
        updateScore(score.scoreWord(w));
        words.use(w);
      } else {
        showMessage("“" + w + "” not in word list.", Color.red);
      }
      words.clearWord();
    }
  }
}
