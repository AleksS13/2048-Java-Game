package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Represents the main game logic for the 2048 game.
 * This class manages the game state, including the game board, score, and high scores.
 */
public class Game {
	private Board board; // The game board
    int score; // Current score of the game
    static int highScore = 0; // Static variable to hold the highest score across games
    private int loadedSize; // Size of the board when a game is loaded
    static ArrayList<Integer> scores = new ArrayList<>(); // List of all scores
    static public ArrayList<String> savedGames = new ArrayList<>(); // List of saved games

	 /**
     * Constructs a new Game with a fresh board of a specified size.
     *
     * @param size The size of the board (4x4 or 5x5).
     */
	public Game(int size) {
		this.board = new Board(size);
		this.score = 0;
	}

	/**
     * Constructs a Game by loading its state from a saved file.
     *
     * @param filename The name of the file from which to load the game.
     */
	public Game(String filename) {
		loadGame(filename);
	}

	/**
	 * Returns the size of the current game board.
	 *
	 * @return The size of the game board.
	 */
	public int getBoardSize() {
		return board.size;
	}

	/**
	 * Executes a move action by moving tiles upwards.
	 * Updates the score after the move.
	 */
	public void moveUp() {
		board.moveUp();
		score = board.score;
	}

	/**
	 * Executes a move action by moving tiles down.
	 * Updates the score after the move.
	 */
	public void moveDown() {
		board.moveDown();
		score = board.score;
	}

	/**
	 * Executes a move action by moving tiles left.
	 * Updates the score after the move.
	 */
	public void moveLeft() {
		board.moveLeft();
		score = board.score;
	}

	/**
	 * Executes a move action by moving tiles right.
	 * Updates the score after the move.
	 */
	public void moveRight() {
		board.moveRight();
		score = board.score;
	}
	
	/**
	 * Checks if the player has reached the 2048 tile.
	 *
	 * @return True if the player has created a 2048 tile, false otherwise.
	 */
	public boolean hasReached2048() {
		return board.hasReached2048;
	}

	/**
	 * Determines if the game is over.
	 * The game is over if the player chooses not to continue after reaching 2048,
	 * or if there are no more possible moves.
	 *
	 * @param answer Indicates whether the player wants to continue after reaching 2048.
	 * @return True if the game is over, false otherwise.
	 */
	public boolean isGameOver(boolean answer) {
		if (answer) {
			board.hasReached2048 = false;
		}

		if (!answer || !board.canMakeMove()) {
			board.hasReached2048 = false;
			return true;
		}
		return false;
	}

	/**
	 * Returns the current score of the game.
	 *
	 * @return The current score.
	 */
	public int getScore() {
		return score;
	}

	public static void saveScore(int score) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("Score.txt", true));
			writer.write(score + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * Retrieves and returns the highest score from the saved scores file.
     *
     * @return The highest score recorded.
     */
	public static int getHighScore() {
		getAllScores();
		if (scores.size() > 0) {
			scores.sort(Comparator.reverseOrder());
			highScore = scores.get(0);
		}

		return highScore;
	}

	/**
     * Loads all saved scores from the file into the scores list.
     * This method is used to populate the list of scores for high score calculation.
     */
	public static void getAllScores() {
		try {
			File file = new File("Score.txt");
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				scores.add(Integer.parseInt(sc.nextLine()));
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Resets the current game to its initial state.
	 * Clears the board and starts a new game.
	 */
	public void resetGame() {
		board.resetBoard();
	}

	/**
	 * Retrieves the value of a specific tile on the board.
	 *
	 * @param row The row index of the tile.
	 * @param col The column index of the tile.
	 * @return The value of the tile at the specified location.
	 */
	public int getTileValue(int row, int col) {
		return board.getTileValue(row, col);
	}

	/**
     * Saves the current state of the game to a file.
     * This includes the board size, tile values, and current score.
     *
     * @param filename The name of the file to save the game to.
     */
	public void saveGame(String filename) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("savedGames/"+filename + ".txt"))) {

			writer.write(board.size + "\n");

			for (int i = 0; i < board.size; i++) {
				for (int j = 0; j < board.size; j++) {
					writer.write(board.tiles[i][j] + " ");
				}
				writer.write("\n");
			}

			writer.write(score + "\n");

			System.out.println("Game saved successfully.");
			writer.close();
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("savedGames.txt", true));
			if (!savedGames.contains(filename))
				writer2.write(filename + "\n");
			writer2.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the list of saved games from a file.
	 * Populates the static 'savedGames' list with the names of saved game files.
	 */
	static public void savedGames() {
		try (Scanner sc = new Scanner(new File("savedGames.txt"))) {

			while (sc.hasNextLine()) {
				savedGames.add(sc.nextLine());
			}
			sc.close();

		} catch (IOException e) {
			System.out.println("Error: Failed to read savedGames file.");

		}
	}

	 /**
     * Loads a game from a specified file.
     * The method sets up the board and current score based on the file contents.
     *
     * @param filename The name of the file to load the game from.
     */
	public void loadGame(String filename) {
		try (Scanner scanner = new Scanner(new File("savedGames/"+filename + ".txt"))) {

			loadedSize = scanner.nextInt();
			this.board = new Board(loadedSize);

			for (int i = 0; i < loadedSize; i++) {
				for (int j = 0; j < loadedSize; j++) {
					board.tiles[i][j] = scanner.nextInt();
				}
			}

			score = scanner.nextInt();
			board.score = score;

			System.out.println("Game loaded successfully.");
		} catch (IOException | NoSuchElementException e) {
			System.out.println("Error: Failed to load the game.");
			e.printStackTrace();
		}
	}

	/**
	 * Returns the size of the board loaded from a saved game.
	 *
	 * @return The size of the loaded game board.
	 */
	public int getLoadedSize() {
		return loadedSize;
	}

}
