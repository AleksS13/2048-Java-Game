package console;

import java.util.Scanner;

import logic.Board;
import logic.Game;

/**
 * The Play2048 class serves as the console interface for the 2048 game.
 * It provides a text-based interface for interacting with the game logic.
 */
public class Play2048 {
	
	/**
     * The main method that serves as the entry point for the 2048 console application.
     * It provides the game's main menu and manages the game loop.
     *
     * @param args Command line arguments (not used in this application).
     */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Do you want to load a saved game (L) or start a new game (N)?");
		String gameChoice = scanner.next().toUpperCase();
		scanner.nextLine();

		Game game;
		while (true) {
			if (gameChoice.equals("L")) {
				System.out.println("Available games to load: ");
				Game.savedGames();
				for (int i = 0; i < Game.savedGames.size(); i++) {
					System.out.println(Game.savedGames.get(i));
				}

				System.out.println("Enter the name of the game you want to load:");
				String filen = scanner.nextLine();

				game = new Game(filen);
				break;
			} else if (gameChoice.equals("N")) {
				int size = 0;
				while (size != 4 && size != 5) {
					System.out.println(
							"Enter 4 if you want to play on a 4x4 board or 5 if you want to play on a 5x5 board: ");

					try {
						size = scanner.nextInt();

						if (size != 4 && size != 5) {
							System.out.println("Incorrect input. Please enter 4 or 5.");
						}

					} catch (java.util.InputMismatchException e) {
						System.err.println("Error: Invalid input. Please enter a valid integer.");
						scanner.nextLine(); 
					} finally {
						scanner.nextLine(); 
					}
				}

				System.out.println("You selected a " + size + "x" + size + " board.");
				game = new Game(size);
				break;
			} else {
				System.out.println("Invalid choice.");
			}
		}

		boolean answer = true;
		
		System.out.println("A - Left, D - right, W - up, s - down, E - pause, P - save game");
		while (true) {
			displayBoard(game, game.getBoardSize());

			String userInput = scanner.next().toUpperCase();
			if (userInput.equals("E")) {
				end(game.getScore());
				break;
			}

			else
				move(userInput, game);
			if (game.hasReached2048())
				answer = doesUserWantToContinue(answer);

			if (game.isGameOver(answer)) {
				end(game.getScore());
				break;
			}
		}

		scanner.close();

	}
	
	/**
     * Displays the current state of the game board in the console.
     *
     * @param game The game instance whose board is to be displayed.
     * @param size The size of the game board.
     */
	public static void displayBoard(Game game,int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print( game.getTileValue(i, j)+ "\t");
			}
			System.out.println();
		}
	}

	 /**
     * Ends the game session, displaying the final score and high score.
     *
     * @param score The final score achieved in the game.
     */
	private static void end(int score) {

		System.out.println("GAME OVER\n" + "Your score is:" + score);

		Game.saveScore(score);
		System.out.println("High score: " + Game.getHighScore());

	}

	private static boolean doesUserWantToContinue(boolean answer) {

		while (true) {
			System.out.println("Do you want to continue playing the game? YES/NO");
			Scanner scanner = new Scanner(System.in);
			try {
				String answerInput = scanner.next().toUpperCase();
				if (answerInput.equals("NO")) {
					answer = false;
					break;
				} else if (answerInput.equals("YES")) {
					answer = true;
					break;
				} else {
					scanner.close();
					throw new IllegalArgumentException("Invalid input. Please enter YES or NO.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			scanner.close();
		}
		return answer;
	}

	
	 /**
     * Processes the player's input to move the tiles in the game.
     * Supports moving tiles up, down, left, and right, and saving the game.
     *
     * @param input The player's input command.
     * @param game The current game instance to be manipulated.
     */
	private static void move(String input, Game game) {
		switch (input) {
		case "W":
			game.moveUp();
			break;
		case "S":
			game.moveDown();
			break;
		case "A":
			game.moveLeft();
			break;
		case "D":
			game.moveRight();
			break;
		case "P":
			Scanner sc = new Scanner(System.in);

			System.out.println("Enter file name:");
			String filename = sc.nextLine();
			game.saveGame(filename);
			break;
		}
	}

}
