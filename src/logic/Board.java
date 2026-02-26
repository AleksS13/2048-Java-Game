package logic;

import java.util.Arrays;
import java.util.Random;

/**
 * Represents the game board for the 2048 game.
 * It manages the tiles, their movements, and the game's scoring logic.
 */
public class Board {
	public int size; // The size of the game board
    public int[][] tiles; // 2D array representing the tiles on the board
    boolean hasReached2048 = false; // Flag to check if 2048 is reached
    int score = 0; // Current score of the game

    /**
     * Constructs a new Board with the specified size.
     *
     * @param size The size of the game board, typically 4x4 or 5x5.
     */
	public Board(int size) {
		this.size = size;
		this.tiles = new int[size][size];
		addRandomTile();
		addRandomTile();
	}

	/**
     * Adds a random tile (either 2 or 4) to an empty position on the board.
     */
	private void addRandomTile() {
		Random random = new Random();
		int value;// Generate either 2 or 4
		int emptyTileCount = countEmptyTiles();
		if (emptyTileCount >= size * size - 1)
			value = 2;
		else
			value = (random.nextInt(2) + 1) * 2;

		if (emptyTileCount > 0) {
			int randomIndex = random.nextInt(emptyTileCount);
			int count = 0;

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (tiles[i][j] == 0) {
						if (count == randomIndex) {
							tiles[i][j] = value;
							return;
						}
						count++;
					}
				}
			}
		}
	}

	 /**
     * Counts the number of empty tiles on the board.
     *
     * @return The count of empty tiles.
     */
	private int countEmptyTiles() {
		int count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tiles[i][j] == 0) {
					count++;
				}
			}
		}
		return count;
	}

	/**
     * Retrieves the value of the tile at the specified position.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     * @return The value of the tile at the specified position.
     */
	public int getTileValue(int row, int col) {
		return tiles[row][col];
	}

	/**
     * Moves the tiles on the board up and merges them if applicable.
     * Adds a new random tile if the board changes.
     */
	public void moveUp() {
		int[][] temp = new int[size][size];
	    for (int i = 0; i < size; i++) {
	        System.arraycopy(tiles[i], 0, temp[i], 0, tiles[i].length);
	    }
		for (int j = 0; j < size; j++) {
			int column[] = new int[size];

			for (int i = 0; i < size; i++) {
				column[i] = tiles[i][j];
			}

			mergeTiles(column);

			for (int i = 0; i < size; i++) {
				tiles[i][j] = column[i];
			}
		}
		if (!Arrays.deepEquals(tiles,temp))
			addRandomTile();
	}

	/**
     * Moves the tiles on the board down and merges them if applicable.
     * Adds a new random tile if the board changes.
     */
	public void moveDown() {
		int[][] temp = new int[size][size];
	    for (int i = 0; i < size; i++) {
	        System.arraycopy(tiles[i], 0, temp[i], 0, tiles[i].length);
	    }
		for (int j = 0; j < size; j++) {
			int column[] = new int[size];

			for (int i = size - 1; i >= 0; i--) {
				column[size - 1 - i] = tiles[i][j];
			}

			mergeTiles(column);

			for (int i = size - 1; i >= 0; i--) {
				tiles[i][j] = column[size - 1 - i];
			}
		}
		if (!Arrays.deepEquals(tiles,temp))
			addRandomTile();

	}
	
	/**
     * Moves the tiles on the board to the left and merges them if applicable.
     * Adds a new random tile if the board changes.
     */
	public void moveLeft() {
		int[][] temp = new int[size][size];
	    for (int i = 0; i < size; i++) {
	        System.arraycopy(tiles[i], 0, temp[i], 0, tiles[i].length);
	    }
	    
		for (int i = 0; i < size; i++) {
			int row[] = new int[size];

			for (int j = 0; j < size; j++) {
				row[j] = tiles[i][j];
			}

			mergeTiles(row);

			for (int j = 0; j < size; j++) {
				tiles[i][j] = row[j];
			}
		}
		if (!Arrays.deepEquals(tiles,temp))
			addRandomTile();
	}

	/**
     * Moves the tiles on the board to the right and merges them if applicable.
     * Adds a new random tile if the board changes.
     */
	public void moveRight() {
		int[][] temp = new int[size][size];
	    for (int i = 0; i < size; i++) {
	        System.arraycopy(tiles[i], 0, temp[i], 0, tiles[i].length);
	    }


		for (int i = 0; i < size; i++) {
			int row[] = new int[size];

			for (int j = size - 1; j >= 0; j--) {
				row[size - 1 - j] = tiles[i][j];
			}

			mergeTiles(row);

			for (int j = size - 1; j >= 0; j--) {
				tiles[i][j] = row[size - 1 - j];
			}
		}
		if (!Arrays.deepEquals(tiles,temp))
			addRandomTile();

	}

	/**
     * Merges the tiles in the given array according to the rules of the 2048 game.
     * Updates the score upon merging tiles.
     *
     * @param array An array of tile values representing a row or column.
     */
	private void mergeTiles(int[] array) {
		for (int i = 0; i < size; i++) {
			if (array[i] == 0) {
				for (int j = i + 1; j < size; j++) {
					if (array[j] != 0) {
						array[i] = array[j];
						array[j] = 0;
						break;
					}
				}
			}
			if (array[i] != 0) {
				for (int j = i + 1; j < size; j++) {
					if (array[j] != 0) {
						if (array[i] == array[j]) {
							array[i] *= 2;
							score += array[i];
							array[j] = 0;
							if (array[i] == 2048) {
								hasReached2048 = true;
							}
							break;
						} else if (array[j] != 0 && array[i] != array[j]) {
							break;
						}

					}
				}
			}
		}

	}

	 /**
     * Returns the current score of the game.
     *
     * @return The current score.
     */
	public int getScore() {
		// TODO Auto-generated method stub
		return score;
	}

	/**
     * Resets the board to its initial state with two random tiles.
     */
	public void resetBoard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				tiles[i][j] = 0;
			}
		}

		addRandomTile();
		addRandomTile();
	}

	 /**
     * Checks if there is any possible move left on the board.
     *
     * @return True if a move is possible, false otherwise.
     */
	public boolean canMakeMove() {
		for (int i = 0; i < size; i++) {
			boolean zero = false;
			int nonZero = 0;

			for (int j = 0; j < size; j++) {
				if (tiles[i][j] == 0)
					return true;
				if (tiles[i][j] == nonZero && tiles[i][j] != 0) {
					return true;
				}

				else if (tiles[i][j] == 0) {
					zero = true;
				} else if (tiles[i][j] != 0) {
					nonZero = tiles[i][j];
				}
			}
		}

		for (int j = 0; j < size; j++) {
			boolean zero = false;
			int nonZero = 0;

			for (int i = 0; i < size; i++) {
				// if(tiles[i][j] != 0 && tiles[i][j] != nonZero && nonZero != 0) break;
				if (tiles[i][j] == nonZero && tiles[i][j] != 0) {
					return true;
				}

				else if (tiles[i][j] == 0) {
					zero = true;
				} else if (tiles[i][j] != 0) {
					nonZero = tiles[i][j];
				}
			}
		}

		return false;
	}

}
