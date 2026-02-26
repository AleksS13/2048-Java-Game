package gui;

import javax.swing.*;

/**
 * Main class for the graphical user interface of the 2048 game.
 * It initializes and displays the main menu of the game.
 */
public class Play2048 {

    private JFrame frame;

    /**
     * The main method that serves as the entry point of the application.
     * It initiates the GUI in the Event Dispatch Thread.
     * 
     * @param args Command line arguments, not used in this application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Play2048().displayMainMenu());
    }

    /**
     * Displays the main menu of the game.
     * Sets up the main application window and adds the MainMenu panel to it.
     */
    private void displayMainMenu() {
        frame = new JFrame("Game Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);

        MainMenu startingMainMenu = new MainMenu(this);
        frame.add(startingMainMenu);

        frame.setVisible(true);
    }
}

