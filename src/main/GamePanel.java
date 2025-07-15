package main;

import javax.imageio.ImageIO;
import javax.swing.*;


import background.Manager;
import data.SaveLoad;
import entity.Player;
import entity.PowerUp;
import monster.BlueMonster;
import monster.RedMonster;
import object.Bomb;
import object.Explosion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    public final int originalTileSize = 16;
	public final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxRow = 13;  
    public final int maxCol = 33;
    public final int screenWidth = tileSize * maxCol;
    public final int screenHeight = tileSize * maxRow;
    
    public int countdown = 200;
    private long lastTime = System.nanoTime();
    
    Thread gameThread;
    Thread monsterThread;
    
    public KeyHandler keyHandler = new KeyHandler(this);
    public Manager manage = new Manager(this);
    public Collusion bump = new Collusion(this);
    public ArrayList<Bomb> bombs = new ArrayList<>();
    public ArrayList<RedMonster> monsters = new ArrayList<>();
    public ArrayList<BlueMonster> bmonsters = new ArrayList<>();
    public ArrayList<Explosion> explosions;
    public Player player = new Player(this, keyHandler);
    public PowerUp power = new PowerUp(this);
    public SaveLoad inout = new SaveLoad(this);	
    public Sound sound = new Sound();
    
    public int gameState;
    public final int play = 1;
    public final int stop = 0;
    public final int title = 2;
    public final int settings = 3;
    public final int gameOver = 4;
    public final int won = 5;
    public int musicVolume = 0; 
    
    public int imageCount;
    public int command = 0;
    public int set = 0;
    public int over = 0;
    public int powerSelect = 1;
    public int index = 0;
    
    public static int maxBombs = 1;
    public static int currentBombs = 0;
    public static int countMonsters;
    public static int health = 2;
    public static int points = 0; 
   
    public GamePanel() throws IOException {
    	
    	this.setPreferredSize(new Dimension(screenWidth, screenHeight));        
        this.setDoubleBuffered(true);
        this.keyHandler.gp = this;
        this.setBackground(Color.black);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        gameState = title;
        manage.resetMap();
        manage.random();
        playMusic(0);
        startGameThread();
        spawnMonsters();
    }

    public void startGameThread() {   	
    	gameThread = new Thread(this);
    	gameThread.start();
    }

	@Override
	public void run() {
		while (gameThread.isInterrupted() == false) {
            
			try {
				update();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            repaint();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
		
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2  = (Graphics2D) g;
        
        if(gameState == play) {
	        manage.paint(g2);
	        player.paintComponent(g2);
	        if(!player.powerImage)
	        	power.paint(g2);	
	        for (RedMonster monster : monsters) {
	            monster.paintComponent(g2);
	        }
	        for(BlueMonster monster: bmonsters) {
	        	monster.paintComponent(g2);
	        }
	    	for (Bomb bomb : bombs) {
	    		bomb.paint(g2);         
	    	}        
	        drawTimer(g2);
	        drawPoints(g2);
	        try {
				drawHealth(g2);
			} catch (IOException e) {
				e.printStackTrace();
			}	        	        	        
        }
        if(gameState == stop) {
        	manage.paint(g2);
	        player.paintComponent(g2);
	        for (RedMonster monster : monsters) {
	            monster.paintComponent(g2);
	        }
	    	for (Bomb bomb : bombs) {
	    		bomb.paint(g2);         
	    	}        
	        drawTimer(g2);
	        drawPoints(g2);
        	drawPauseScreen(g2);
		}
        if(gameState == title) {
        	drawTitleScreen(g2);
        }
        if(gameState == settings) {
        	drawSettingsScreen(g2);
        }
        if(gameState == gameOver) {
        	drawGameOver(g2);
        }
        if(gameState == won) {
        	drawWonScreen(g2);
        }
    }
	
	
	public void update() throws IOException {
		if(gameState == play) {
			player.update();
			updateBombs();
			updateTimer();
			updateMonsters();
			power.changeCharacter();
		}
	}
	
	public void placeBomb() {       
		boolean intersects = false;
		if (currentBombs < maxBombs) {
            int bombX = (player.area.x/tileSize) * tileSize;
            int bombY = (player.area.y/tileSize) * tileSize;
            for(int i = 0; i < currentBombs; i++) {
            	if(new Bomb(this, bombX, bombY).getHitbox().intersects(bombs.get(i).getHitbox())) {
            		intersects = true;
            	}
            }
            if(!intersects) {
            	bombs.add(new Bomb(this, bombX, bombY));
            	currentBombs++;             	
            }
        }
    }
	
	public void updateBombs() throws IOException {       							
			for (int i = 0; i < bombs.size(); i++) {
	            Bomb bomb = bombs.get(i);
	            bomb.update();
	            if (bomb.isExploded() && bomb.exploded && bomb.explosion.isExpired()) {	                
	            	bombs.remove(i);
	                i--;
	            }
			}
    }
	
	public void updateTimer() {
        long currentTime = System.nanoTime();
        if (currentTime - lastTime >= 1000000000) {
            if (countdown > 0) {
                countdown--;
            }
            lastTime = currentTime;
        }
        if(countdown == 0) {
        	gameState = gameOver;
        }
    }
	
	public void drawTimer(Graphics2D g2) {
	    g2.setColor(Color.BLACK);
	    g2.setFont(new Font("Arial", Font.BOLD, 20));
	    g2.drawString("Time: " + countdown, 1380, 30);
	}
	
	public void drawPoints(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Arial", Font.BOLD, 20));
	    g2.drawString("" + points, 750, 30);
	}
	
	public void drawHealth(Graphics2D g2) throws IOException {
		BufferedImage image = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/object/health1.png")));
		for(int i = 0; i < health + 1; i++)
			g2.drawImage(image, i * tileSize, -20, 2 * tileSize, 2* tileSize, null);
	}
	public void drawPauseScreen(Graphics2D g) {
		Font font = new Font(null, Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.white);
		String text = "Paused";
		int x = screenWidth / 2 - 150;
		int y = screenHeight / 2;
		g.drawString(text, x, y);		
	}
	
	public void drawTitleScreen(Graphics2D g) {
		g.setFont(new Font(null, Font.BOLD, 50));
		g.setColor(Color.black);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.setColor(Color.white);
		g.drawString("Bomberman", 600, 100);
		
		g.setFont(new Font(null, Font.BOLD, 30)); 
		
		g.drawString("New Game", 665, 275);
		if(command == 0)
			g.drawString(">", 665 - tileSize, 275);
		
		g.drawString("Resume", 680, 350);
		if(command == 1)
			g.drawString(">", 680 - tileSize, 350);
		
		g.drawString("Load Game", 660, 425);
		if(command == 2)
			g.drawString(">", 660 - tileSize, 425);
		
		g.drawString("Settings", 680, 500);
		if(command == 3)
			g.drawString(">", 680 - tileSize, 500);
		
		g.drawString("Quit", 705, 575);
		if(command == 4)
			g.drawString(">", 705 - tileSize, 575);
	}
	
	public void drawSettingsScreen(Graphics2D g) {
		powerSelect = 0;
		int length = 200;
		
		g.setFont(new Font(null, Font.BOLD, 40));
		g.setColor(Color.white);
		g.drawString("Select Power", 300, 100);
		g.setFont(new Font(null, Font.BOLD, 20));
		g.setColor(Color.red);
		g.drawString("Red (gives control to bomb explosion time)", 300, length);
		if(set == 0) {
			g.drawString(">", 300 - tileSize, length);
			powerSelect = 0;
		}
		g.setColor(Color.yellow);
		g.drawString("Yellow (allows more bombs)", 300, length + 50);
		if(set == 1) {
			g.drawString(">", 300 - tileSize, length + 50);
			powerSelect = 1;
		}
		g.setColor(Color.green);
		g.drawString("Green (allows larger bomb exlosions)", 300, length + 100);
		if(set == 2) {
			g.drawString(">", 300 - tileSize, length + 100);
			powerSelect = 2;
		}
		g.setColor(Color.blue);
		g.drawString("Blue (allows passing weak walls)", 300, length + 150);
		if(set == 3) {
			g.drawString(">", 300 - tileSize, length + 150);
			powerSelect = 3;
		}
		g.setColor(Color.cyan);
		g.drawString("Cyan (increases speed)", 300, length + 200);
		if(set == 4) {
			g.drawString(">", 300 - tileSize, length + 200);
			powerSelect = 4;
		}
		
		g.setColor(Color.white);
	    g.drawString("Music Volume: " + musicVolume / 10, 1000, 150);
	    if (set == 5) {
	        g.drawString(">", 1000 - tileSize, 150);
	    }
	   
	    g.setColor(Color.white);
	    g.drawString("Character Select", 1000, 250);
	    if (set == 6) {
	        g.drawString("<", 1000 - tileSize, 250);
	        g.drawString(">", 1200, 250);
	    }
	    if (player.imageOrder == 0) {
	        player.getImage();
	        g.drawImage(player.down1, 1025, 300, tileSize * 2, tileSize * 2, null);
	    } else if (player.imageOrder == 1) {
	        player.getImage();
	        g.drawImage(player.down1, 1025, 300, tileSize * 2, tileSize * 2, null);
	    } else if (player.imageOrder == 2) {
	        player.getImage();
	        g.drawImage(player.down1, 1025, 300, tileSize * 2, tileSize * 2, null);
	    } else if (player.imageOrder == 3) {
	        player.getImage();
	        g.drawImage(player.down1, 1025, 300, tileSize * 2, tileSize * 2, null);
	    }
	}
	
	public void drawGameOver(Graphics g) {
		g.setColor(Color.black);
    	g.fillRect(0, 0, screenWidth, screenHeight);
    	
    	g.setColor(Color.white);
    	g.setFont(new Font(null, Font.BOLD, 100));
    	g.drawString("Game Over", 450, 200);
    	
    	g.setFont(new Font(null, Font.BOLD, 80));
    	g.drawString("Retry", 600, 375);
    	if(over == 0) {
    		g.drawString(">", 550, 375);
    	}
    	g.drawString("Quit", 600, 450);
    	if(over == 1) {
    		g.drawString(">", 550, 450);
    	}
    	
	}
	
	public void drawWonScreen(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.setColor(Color.white);
		g.setFont(new Font(null, Font.BOLD, 50));
		g.drawString("You Won!", 630, 200);
		g.drawString("Main Menu", 620, 450);
		g.drawString("Next Level", 620, 400);
		if(index == 0)
			g.drawString(">", 575, 450);
		if(index == 1)
			g.drawString(">", 575, 400);
	}
	public void spawnMonsters() throws IOException {
        for (int i = 0; i < 6; i++) {
            monsters.add(new RedMonster(this));
            countMonsters++;
            monsterThread = new Thread(monsters.get(i));
			monsterThread.start();
        }
    }
	
	public void updateMonsters() {
		if (gameState == play) {
	        for (int i = 0; i < monsters.size(); i++) {
	            monsters.get(i).update();
	        }
	    }
	}
	
	public void spawnBlueMonsters() throws IOException {
		for (int i = 0; i < 7; i++) {
            bmonsters.add(new BlueMonster(this));
            countMonsters++;
            monsterThread = new Thread(bmonsters.get(i));
			monsterThread.start();
        }
	}
	
	public void updateBlueMonsters() {
		if (gameState == play) {
	        for (int i = 0; i < bmonsters.size(); i++) {
	            bmonsters.get(i).update();
	        }
	    }
	}
	
	public void playMusic(int i) {
		sound.setFile(i);
		sound.play();
		sound.setVolume(musicVolume);
		sound.loop();
	}
	
	public void stopMusic() {
		sound.stop();
	}
	
	public void soundEffect(int i) {
		sound.setFile(i);
		sound.play();
		sound.setVolume(100);
	}
}

