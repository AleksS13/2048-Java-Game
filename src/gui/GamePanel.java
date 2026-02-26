package gui;

import javax.swing.*;

import logic.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

/**
 * Represents the graphical user interface for the 2048 game. This class handles
 * the display and interaction of the game board, including the visualization of
 * tiles, score, and controls for the game.
 */
public class GamePanel extends JFrame {

	private Game game; // The game logic handler
	public int size; // Size of the game board
	private JLabel[][] tileLabels; // Labels for displaying tile values
	private JLabel scoreLabel; // Label for displaying the current score
	private JLabel bestScoreLabel; // Label for displaying the best score
	private boolean answer = true; // Flag for game continuation after reaching 2048
	private boolean isGameLoaded = false; // Flag for checking if a game is loaded
	private String loadedGame = ""; // Filename of the loaded game

	/**
	 * Constructs a GamePanel for a new game with the specified size. Initializes
	 * the game and sets up the user interface components.
	 *
	 * @param size The size of the game board (either 4x4 or 5x5).
	 */
	public GamePanel(int size) {
		this.size = size;

		game = new Game(size);
		tileLabels = new JLabel[size][size];

		GamePanelSettings();
		updateBoard();
		// move();
		setupKeyBindings();

		setPreferredSize(new Dimension(500, 500));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Constructs a GamePanel by loading a saved game from a file. Initializes the
	 * game with the loaded state and sets up the user interface.
	 *
	 * @param selectedGame The filename of the saved game to load.
	 */
	public GamePanel(String selectedGame) {
		game = new Game(selectedGame);
		this.size = game.getLoadedSize();
		isGameLoaded = true;
		loadedGame = selectedGame;

		tileLabels = new JLabel[size][size];

		GamePanelSettings();
		updateBoard();
		// move();
		setupKeyBindings();

		setPreferredSize(new Dimension(500, 500));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Configures the basic settings of the game panel. Sets the layout, background
	 * color, and initializes the game components.
	 */
	private void GamePanelSettings() {
		setTitle("2048 Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(20, 20));
		getContentPane().setBackground(new Color(250, 248, 239));

		add(createHeaderPanel(), BorderLayout.NORTH);
		add(createBoardPanel(), BorderLayout.CENTER);
	}

	/**
	 * Sets up key bindings for controlling the game using the keyboard. Assigns
	 * actions for arrow key presses to handle tile movements.
	 */
	private void setupKeyBindings() {
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = getRootPane().getInputMap(condition);
		ActionMap actionMap = getRootPane().getActionMap();

		// Define key strokes
		KeyStroke upKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
		KeyStroke downKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
		KeyStroke leftKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
		KeyStroke rightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);

		// Bind key strokes to actions
		inputMap.put(upKey, "MOVE_UP");
		inputMap.put(downKey, "MOVE_DOWN");
		inputMap.put(leftKey, "MOVE_LEFT");
		inputMap.put(rightKey, "MOVE_RIGHT");

		actionMap.put("MOVE_UP", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp();
			}
		});
		actionMap.put("MOVE_DOWN", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown();
			}
		});
		actionMap.put("MOVE_LEFT", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveLeft();
			}
		});
		actionMap.put("MOVE_RIGHT", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveRight();
			}
		});
	}

	/**
	 * Handles the action for moving the tiles up. Updates the game board and checks
	 * for game continuation conditions.
	 */
	private void moveUp() {
		game.moveUp();
		updateAfterMove();
	}

	/**
	 * Handles the action for moving the tiles down. Updates the game board and
	 * checks for game continuation conditions.
	 */
	private void moveDown() {
		game.moveDown();
		updateAfterMove();
	}

	/**
	 * Handles the action for moving the tiles left. Updates the game board and
	 * checks for game continuation conditions.
	 */
	private void moveLeft() {
		game.moveLeft();
		updateAfterMove();
	}

	/**
	 * Handles the action for moving the tiles right. Updates the game board and
	 * checks for game continuation conditions.
	 */
	private void moveRight() {
		game.moveRight();
		updateAfterMove();
	}

	/**
	 * Updates the game state and UI after each move. Checks whether the game should
	 * continue or end, and updates the board and score display.
	 */
	private void updateAfterMove() {
		updateBoard();
		scoreLabel.setText("SCORE: " + game.getScore());
		if (game.getScore() > Game.getHighScore())
			bestScoreLabel.setText("BEST: " + game.getScore());
		checkForContinue();
		if (!answer || game.isGameOver(answer)) {
			noMorePossibleMoves();
		}
	}

	/**
	 * Creates and returns the header panel with the game title, score, and best
	 * score labels.
	 *
	 * @return JPanel The configured header panel.
	 */
	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		JButton saveButton = new JButton("Save");
		headerPanel.setLayout(new GridLayout(1, 3, 10, 10));
		headerPanel.setBackground(new Color(250, 248, 239));

		JLabel gameTitleLabel = createLabel("2048", new Color(237, 197, 63), new Font("Arial", Font.BOLD, 36));
		scoreLabel = createLabel("SCORE: " + game.getScore(), new Color(250, 248, 239),
				new Font("Arial", Font.BOLD, 12));
		bestScoreLabel = createLabel("BEST: " + Game.getHighScore(), new Color(250, 248, 239),
				new Font("Arial", Font.BOLD, 12));

		saveButton.setBackground(new Color(187, 173, 160));
		saveButton.setForeground(Color.WHITE);
		saveButton.setFont(new Font("Arial", Font.BOLD, 14));
		saveButton.setFocusPainted(false);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filename = promptForFileName();
				if (filename != null) {
					Game.saveScore(game.getScore());
					game.saveGame(filename);
				}
			}
		});

		headerPanel.add(gameTitleLabel);
		headerPanel.add(scoreLabel);
		headerPanel.add(bestScoreLabel);
		headerPanel.add(saveButton);

		return headerPanel;
	}

	/**
	 * Creates and configures a JLabel with specified text, background color, and
	 * font.
	 *
	 * @param text       The text for the label.
	 * @param background The background color of the label.
	 * @param font       The font of the text.
	 * @return JLabel The configured label.
	 */
	private JLabel createLabel(String text, Color background, Font font) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setOpaque(true);
		label.setBackground(background);
		label.setFont(font);
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return label;
	}

	/**
	 * Creates and returns the main board panel with tile labels. Configures the
	 * layout and appearance of the game board.
	 *
	 * @return JPanel The panel representing the game board.
	 */
	private JPanel createBoardPanel() {
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(size, size, 10, 10));
		boardPanel.setBackground(new Color(187, 173, 160));
		boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				tileLabels[i][j] = createLabel("", new Color(205, 193, 180), new Font("Arial", Font.BOLD, 24));
				boardPanel.add(tileLabels[i][j]);
			}
		}

		return boardPanel;
	}

	/**
	 * Updates the board display based on the current state of the game. Updates the
	 * tile values and colors to reflect the current game state.
	 */
	private void updateBoard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int value = game.getTileValue(i, j);
				JLabel label = tileLabels[i][j];
				if (value > 0) {
					label.setText(String.valueOf(value));
					label.setBackground(getTileColor(value));
				} else {
					label.setText("");
					label.setBackground(new Color(205, 193, 180));
				}
			}
		}
	}

	/**
	 * Determines and returns the color associated with a specific tile value.
	 * Different tile values have distinct colors for visual distinction on the game
	 * board.
	 *
	 * @param value The value of the tile.
	 * @return The color associated with the given tile value.
	 */
	private Color getTileColor(int value) {
		switch (value) {
		case 2:
			return new Color(238, 228, 218);
		case 4:
			return new Color(237, 224, 200);
		case 8:
			return new Color(242, 177, 121);
		case 16:
			return new Color(245, 149, 99);
		case 32:
			return new Color(246, 124, 95);
		case 64:
			return new Color(246, 94, 59);
		case 128:
			return new Color(237, 207, 114);
		case 256:
			return new Color(237, 204, 97);
		case 512:
			return new Color(237, 200, 80);
		case 1024:
			return new Color(237, 197, 63);
		case 2048:
			return new Color(237, 194, 46);

		default:
			return new Color(205, 193, 180);
		}
	}

	/**
	 * Prompts the user to enter a filename for saving the game. Offers options to
	 * either save the game or save and exit. If the game is loaded from a file, it
	 * can be directly saved without entering a new filename.
	 *
	 * @return The filename entered by the user or null if no valid input is
	 *         provided.
	 */
	private String promptForFileName() {
		JTextField filenameField = new JTextField();
		if (isGameLoaded)
			filenameField.setEnabled(true);

		Object[] message = { "Enter the name of the file to save the game:", filenameField };

		Object[] options = { "Save", "Save and Exit" };

		int option = JOptionPane.showOptionDialog(this, message, "Save Game", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.YES_OPTION) {
			String filename;
			if (isGameLoaded)
				filename = loadedGame;
			else
				filename = filenameField.getText();
			if (!filename.isEmpty()) {
				return filename;
			} else {
				JOptionPane.showMessageDialog(this, "You must enter a file name.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return null;
			}
		} else if (option == JOptionPane.NO_OPTION) {
			String filename;
			if (isGameLoaded)
				filename = loadedGame;
			else
				filename = filenameField.getText();
			if (!filename.isEmpty()) {
				game.saveGame(filename);
				System.exit(0);
				return filename;
			} else {
				JOptionPane.showMessageDialog(this, "You must enter a file name to save and exit.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return null;
			}
		}
		return null;
	}

	/**
	 * Checks if the player has reached the 2048 tile and prompts them to choose
	 * whether to continue playing. Sets the 'answer' flag based on the user's
	 * decision.
	 */
	public void checkForContinue() {
		if (game.hasReached2048()) {
			int option = JOptionPane.showConfirmDialog(this, "You've reached 2048! Do you want to continue playing?",
					"Game Reached 2048", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (option == JOptionPane.NO_OPTION) {
				this.answer = false;
				game.isGameOver(answer);
				endGame();
			}
		}
	}

	/**
	 * Displays a dialog when there are no more possible moves left. Offers the
	 * player the option to start a new game or end the current game. Handles the
	 * user's choice accordingly.
	 */
	public void noMorePossibleMoves() {
		int option = JOptionPane.showConfirmDialog(this,
				"No more possible moves. Do you want to start a new game? \n Score: " + game.getScore(), "Game Over",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

		if (option == JOptionPane.YES_OPTION) {

			startNewGame();
		} else {

			endGame();

		}
	}

	/**
	 * Handles the logic for starting a new game. Saves the current game score and
	 * disposes of the current game panel.
	 */
	private void startNewGame() {
		Game.saveScore(game.getScore());
		dispose();
	}

	/**
	 * Handles the logic for ending the game. Saves the current game score and exits
	 * the application.
	 */
	private void endGame() {
		Game.saveScore(game.getScore());
		System.exit(0);
	}

}