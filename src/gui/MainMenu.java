package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import logic.Game;

/**
 * Represents the main menu panel of the 2048 game.
 * This class provides options for the player to start a new game or load an existing game.
 * It handles the user interactions for selecting the game mode.
 */

class MainMenu extends JPanel {
    private final Play2048 play2048;
    private JButton button1;
    private JButton button2;
    private String selectedGame;
    private static JComboBox<String> savedGamesComboBox;
    
    /**
     * Constructs the main menu panel.
     * Sets up the layout and initializes the components for the game selection.
     *
     * @param play2048 The main frame of the application which this panel is part of.
     */    
    public MainMenu(Play2048 play2048) {
        this.play2048 = play2048;
        setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        JLabel jlabel = new JLabel("Do you want to load a saved game or start a new game?", SwingConstants.CENTER);
        jlabel.setFont(new Font("Verdana", Font.BOLD, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.set(10, 10, 10, 10);

        add(jlabel, gbc);

        button1 = new JButton("Load game");
        button1.setBackground(new Color(255, 170, 128));
        button1.setFocusPainted(false); 

        GridBagConstraints gbcButton1 = new GridBagConstraints();
        gbcButton1.gridx = 0;
        gbcButton1.gridy = 1;
        gbcButton1.insets.set(10, 10, 10, 10);

        add(button1, gbcButton1);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               modifyForLoadGameOptions();
            }
        });

        button2 = new JButton("New game");
        button2.setBackground(new Color(255, 170, 128));

        GridBagConstraints gbcButton2 = new GridBagConstraints();
        gbcButton2.gridx = 0;
        gbcButton2.gridy = 2;
        gbcButton2.insets.set(10, 10, 10, 10);
        button2.setFocusPainted(false); 

        add(button2, gbcButton2);
        
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyForBoardSizeOptions();
            }
        });
    }

    /**
     * Modifies the panel to display options for selecting the board size for a new game.
     * Provides buttons for the user to choose between different board sizes.
     */
    private  void modifyForBoardSizeOptions() {
        this.removeAll(); // Clear existing components

        // Update the label with board size options
        this.add(new JLabel("Choose board size:", SwingConstants.CENTER), createConstraints(0, 0));
        JButton b1 = new JButton("5x5");
        b1.setFocusPainted(false); 
        
        JButton b2 = new JButton("4x4");
        b2.setFocusPainted(false); 
        b1.setBackground(new Color(255, 170, 128));
        b2.setBackground(new Color(255, 170, 128));
        this.add(b1, createConstraints(0, 1));
        this.add(b2, createConstraints(0, 2));
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Modify the existing MainMenu instance to display board size options
            	 SwingUtilities.invokeLater(() -> new GamePanel(5));
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Modify the existing MainMenu instance to display board size options
            	 SwingUtilities.invokeLater(() -> new GamePanel(4));
            }
        });

        this.revalidate();
       this.repaint();
    }
    
    /**
     * Modifies the panel to display options for loading an existing game.
     * Provides a combo box for the user to select from available saved games and a button to load the selected game.
     */
    private void modifyForLoadGameOptions() {
        this.removeAll(); 

        
        Game.savedGames();
        ArrayList<String> savedGames = Game.savedGames;
        savedGamesComboBox = new JComboBox<>(savedGames.toArray(new String[0]));

        
        savedGamesComboBox.setBackground(new Color(187, 173, 160));
        savedGamesComboBox.setForeground(Color.WHITE);

        add(savedGamesComboBox);

        JButton loadButton = new JButton("Load Selected Game");
        
       
        loadButton.setBackground(new Color(187, 173, 160));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFont(new Font("Arial", Font.BOLD, 14)); 
        loadButton.setFocusPainted(false);
        loadButton.setBorderPainted(false);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGame = (String) savedGamesComboBox.getSelectedItem();
                SwingUtilities.invokeLater(() -> new GamePanel(selectedGame));
            }
        });

        add(loadButton);

        this.revalidate();
        this.repaint();
    }

    /**
     * Creates and returns GridBagConstraints with specified grid positions and settings for component placement.
     *
     * @param x The grid x position for the component's placement.
     * @param y The grid y position for the component's placement.
     * @return GridBagConstraints The grid bag constraints for the component placement.
     */
    private static GridBagConstraints createConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.set(10, 10, 10, 10);
        return gbc;
    }
   
}


