package background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.PowerUp;
import main.GamePanel;
import object.Explosion;

public class Manager {
	GamePanel gp;
	public Background[] bg;
	public int[][] map;
	Random rand = new Random();
	public int brokenWalls;
	public int doorX = 0;
	public int doorY = 0;
	boolean doorPlaced = false;
	public boolean doorFind = false;
	public boolean powerPlaced = false;
	public boolean powerFind = false;
	public int wallBreakt = 0;
	public static int wallCount = 0;
	public int a = 0;
	
	public Manager(GamePanel gp) {
		this.gp = gp;
		bg = new Background[5];
		map = new int[gp.maxRow][gp.maxCol];
		getImage();
		try {
			map("image/map/map1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getImage() {
		try {
			bg[1] = new Background();
			bg[1].image = ImageIO.read(getClass().getResourceAsStream("/background/strongWall.png"));
			bg[1].bump = true;
			
			bg[2] = new Background();
			bg[2].image = ImageIO.read(getClass().getResourceAsStream("/background/brokenWall.png"));
			bg[2].bump = true;

			bg[0] = new Background();
			bg[0].image = ImageIO.read(getClass().getResourceAsStream("/background/grass.png"));
			
			bg[3] = new Background();
			bg[3].image = ImageIO.read(getClass().getResourceAsStream("/background/door.png"));
			
			bg[4] = new Background();
			PowerUp powera = new PowerUp(gp);
			switch (gp.powerSelect) {
			case 0:
				bg[4].image = powera.yellow;
				break;
			case 1:
				bg[4].image = powera.green;
				break;
			case 2:
				bg[4].image = powera.blue;
				break;
			case 3:
				bg[4].image = powera.red;
				break;
			case 4:
				bg[4].image = powera.cyan;
				break;
							
		}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void map(String str) throws IOException {
		FileReader file = new FileReader(str);
		BufferedReader read = new BufferedReader(file);
		
		int col = 0;
		int row = 0;
		
		while(row < gp.maxRow) {
			String line = read.readLine();
			while(col < gp.maxCol) {
				String[] numbers = line.split(" ");
				map[row][col] = Integer.parseInt(numbers[col]);
				col++;
			}
			if(col == gp.maxCol) {
				col = 0;
				row++;
			}
		}
		read.close();
	}
	
	public void random() {		
		brokenWalls = rand.nextInt(11) + 50;
		int i = 0;
		while(i < brokenWalls) {
			int x = rand.nextInt(gp.maxCol);
	        int y = rand.nextInt(gp.maxRow);
	        
	        if(map[y][x] == 0 && (x != 2 && y != 2) && (x != 1 && y != 1) && (x != 3 || y != 3)) {
	        	map[y][x] = 2;
	        	if(!doorPlaced && i == 1) {
	        		doorX = x * gp.tileSize;
	        		doorY = y * gp.tileSize;
	        		doorPlaced = true;
	        	}
	        	if(!powerPlaced && i == 2) {
	        		 gp.power.placePower(x, y);
	        	}
	        	i++;
	        }
		}
	}
	
	public void paint(Graphics2D g2) {
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;	
				
		while(row < gp.maxRow && col < gp.maxCol) {
			int number = map[row][col];
			
			g2.drawImage(bg[number].image, x, y, gp.tileSize, gp.tileSize, null);		
			col++;
			x += gp.tileSize;
			
			if(col == gp.maxCol) {
				col = 0;
				x = 0;
				row++;
				y += gp.tileSize;
			}
		}
		
		
		if (doorFind) {
	        g2.drawImage(bg[3].image, doorX, doorY, gp.tileSize, gp.tileSize, null);            
	    } else {
	        g2.setColor(Color.red);
	        g2.drawRect(doorX, doorY, gp.tileSize, gp.tileSize);
	    }
		
		if(!powerFind) {
			g2.setColor(Color.yellow);
			g2.drawRect(gp.power.x, gp.power.y, gp.tileSize, gp.tileSize);
		}else {
			if(gp.player.powerImage) {
				map[gp.power.y/gp.tileSize][gp.power.x/gp.tileSize] = 0;
				gp.power.paint(g2);
			}else if(!gp.player.powerImage){				
				map[gp.power.y/gp.tileSize][gp.power.x/gp.tileSize] = 0;
				g2.drawImage(bg[0].image, gp.power.x, gp.power.y, gp.tileSize, gp.tileSize, null);
			}
		}
				
		if(wallBreakt == 1) {
			wallCount++;			
		}
				
	}
	
	public void resetMap() {
		for(int i = 0; i < 11; i++) {
			for(int j = 0; j < 33; j++) {
				if(map[i][j] == 2 || map[i][j] == 3)
					map[i][j] = 0;
			}
		}
		Explosion.hit = 0;
		Explosion.hitp = 0;
		doorPlaced = false;
		doorFind = false;
		powerPlaced = false;
		powerFind = false;
	}
	
	
	public boolean isStrongWall(int row, int col) {
        return map[row][col] == 1;
    }

    public boolean isWeakWall(int row, int col) {
        return map[row][col] == 2;
    }
    
    public boolean isGrass(int row, int col) {
    	return map[row][col] == 0;
    }
    
    public void destroyWeakWall(int row, int col) {
    	map[row][col] = 0;
    }
}

