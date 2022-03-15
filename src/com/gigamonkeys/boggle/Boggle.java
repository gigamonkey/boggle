package com.gigamonkeys.boggle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 * Main Boggle program. Sets up the UI.
 */
class Boggle {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Boggle().makeFrame());
  }

  public static final int GAME_IN_MILLIS = 3 * 60 * 1000;
  public static final int FLASH_MILLIS = 2000;

  public static final int WIDTH = 400;
  public static final int BUTTONS_SIZE = 235;
  public static final int MARGIN = 20;
  public static final int HEIGHT = BUTTONS_SIZE + 150;

  private static final Border whiteline = BorderFactory.createLineBorder(Color.white);

  private static Border defaultBorder = null;
  private static Color defaultButtonColor = null;

  private final Scorekeeper score = new Scorekeeper();
  private final Words words = new Words();
  private final Dice dice = new Dice();
  private final Timer clockTimer = new Timer(1000, e -> updateClock());

  private final JButton[] letterButtons = new JButton[16];
  private final JButton submit = new JButton("Submit");
  private final JLabel clock = new JLabel("0:00", SwingConstants.LEFT);
  private final JLabel scoreboard = new JLabel("Score: 0", SwingConstants.RIGHT);
  private final JLabel message = new JLabel("", SwingConstants.LEFT);

  private long endOfGame = System.currentTimeMillis();
  private Keyboard keyboard = null;

  Boggle() {
    clockTimer.setInitialDelay(0);
  }

  public Point[] diceFor(String text) {
    return IntStream
      .range(0, letterButtons.length)
      .filter(i -> letterButtons[i].getText().equalsIgnoreCase(text))
      .mapToObj(i -> new Point(i % 4, i / 4))
      .toArray(Point[]::new);
  }

  public void highlightButtons(List<List<Point>> currentPossibilities) {
    resetLetterButtons();
    for (var possibility : currentPossibilities) {
      for (var i = 0; i < possibility.size(); i++) {
        var p = possibility.get(i);
        var b = letterButtons[p.y * 4 + p.x];
        if (i == possibility.size() - 1) {
          b.setBackground(Color.gray);
        } else {
          b.setBackground(Color.lightGray);
        }
        b.setBorder(whiteline);
      }
    }
  }

  public void resetLetterButtons() {
    for (var b : letterButtons) {
      resetLetterButton(b);
    }
  }

  public void flashMessage(String msg, Color color) {
    showMessage(msg, color);
    var t = new Timer(FLASH_MILLIS, e -> message.setText(""));
    t.setRepeats(false);
    t.start();
  }

  public void showMessage(String msg, Color color) {
    message.setForeground(color);
    message.setText(msg);
  }

  public void submitWord(String word) {
    if (inGame()) {
      if (word.length() > 0) {
        if (words.alreadyUsed(word)) {
          flashMessage("“" + word + "” already used.", Color.red);
        } else if (words.isWord(word)) {
          flashMessage("“" + word + "” is good!", Color.blue);
          updateScore(score.scoreWord(word));
          words.use(word);
        } else {
          flashMessage("“" + word + "” not in word list.", Color.red);
        }
        resetLetterButtons();
      }
    }
  }

  ////////////////////////////////////////////////////////////////////
  // Setup the UI

  private void makeFrame() {
    JFrame frame = new JFrame("Boggle");
    frame.setSize(WIDTH, HEIGHT);
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setupKeyMap(frame);

    addComponents(frame);
    resetDice(false);
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
  }

  private void setupKeyMap(JFrame frame) {
    for (var c : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
      final String letter = "" + c;
      bind(frame, KeyStroke.getKeyStroke(c), () -> letterTyped(letter));
    }
    bind(frame, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), () -> enter());
  }

  private void bind(JFrame frame, KeyStroke ks, Runnable action) {
    var keys = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    var actions = frame.getRootPane().getActionMap();
    var link = new Object();
    keys.put(ks, link);
    actions.put(
      link,
      new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
          action.run();
        }
      }
    );
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
    var panel = new JPanel(new GridLayout(4, 4));
    panel.setOpaque(false);

    Dimension d = new Dimension(BUTTONS_SIZE, BUTTONS_SIZE);
    panel.setPreferredSize(d);
    panel.setMaximumSize(d);

    for (var i = 0; i < 16; i++) {
      var p = new Point(i % 4, i / 4);
      var b = new JButton("");
      b.addActionListener(e -> buttonPressed(p, b.getText()));
      b.setEnabled(false);
      b.setOpaque(true);
      if (defaultBorder == null) {
        defaultBorder = b.getBorder();
        defaultButtonColor = b.getBackground();
      }
      panel.add(b);
      letterButtons[i] = b;
    }
    return panel;
  }

  private void buttonPressed(Point p, String text) {
    if (keyboard.isPressPossible(p)) {
      keyboard.letterPressed(p, text);
    } else {
      illegalPress(p);
    }
  }

  private void letterTyped(String letter) {
    keyboard.letterTyped(letter);
    highlightButtons(keyboard.getPossibilities());
    showMessage(keyboard.getWord(), Color.black);
  }

  private void enter() {
    if (keyboard.stillPossible()) {
      submitWord(keyboard.getWord());
    } else {
      flashMessage("Can't make " + keyboard.getWord(), Color.red);
    }
    keyboard.resetWord();
    resetLetterButtons();
  }

  private void illegalPress(Point p) {
    var i = p.y * 4 + p.x;
    var b = letterButtons[i];
    var end = System.currentTimeMillis() + 200;
    var start = b.getLocation();
    Timer t = new Timer(
      64,
      e -> {
        if (System.currentTimeMillis() < end) {
          var s = b.getLocation().x >= start.x ? -1 : 1;
          b.setLocation(start.x + (2 * s), start.y);
        } else {
          b.setLocation(start);
          ((Timer) e.getSource()).stop();
        }
      }
    );
    t.setInitialDelay(0);
    t.start();
  }

  private Component submitButton() {
    Dimension d = new Dimension((int) (BUTTONS_SIZE * 0.95), 20);
    submit.setPreferredSize(d);
    submit.setMaximumSize(d);
    submit.addActionListener(e -> enter());
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
    for (var b : letterButtons) {
      b.setEnabled(false);
    }
    submit.setEnabled(false);
    showMessage("Game over.", Color.gray);
  }

  private boolean inGame() {
    return System.currentTimeMillis() < endOfGame;
  }

  private void resetLetterButton(JButton b) {
    b.setBorder(defaultBorder);
    b.setBackground(defaultButtonColor);
  }

  private void startClock() {
    endOfGame = System.currentTimeMillis() + GAME_IN_MILLIS;
    clockTimer.restart();
  }

  private void updateClock() {
    var s = Math.max(0, Math.round((endOfGame - System.currentTimeMillis()) / 1000.0));
    var minutes = s / 60;
    var seconds = s % 60;
    clock.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : "" + seconds));
    if (s == 0) gameOver();
  }

  private void resetDice(boolean enable) {
    var faces = dice.faces(Dice.MODERN);
    keyboard = new Keyboard(faces);
    Solver s = new Solver(faces, words);
    if (enable) {
      s.legalWords().forEach(w -> System.out.println(w));
    }
    for (var i = 0; i < letterButtons.length; i++) {
      letterButtons[i].setText(faces.get(i));
      letterButtons[i].setEnabled(enable);
      resetLetterButton(letterButtons[i]);
    }
  }
}
