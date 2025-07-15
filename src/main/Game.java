package main;

import javax.swing.*;
import java.io.IOException;

public class Game extends JFrame {
    private GamePanel gamePanel;
    
    public Game() throws IOException {
    	gamePanel = new GamePanel();
    	setTitle("Bomberman");
        setSize(gamePanel.screenWidth, gamePanel.screenHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(gamePanel);
    }

    public static void main(String[] args) throws IOException {       
    	Game game = new Game();
        game.setVisible(true);    
    }
}

