package monster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;
import object.Bomb;
import object.Explosion;

public class RedMonster extends Entity implements Runnable {
	
	GamePanel gp;
	boolean animation;
	int clock, count;
	public boolean isAlive = true;
	Random random = new Random();
	
	public RedMonster(GamePanel gp) throws IOException {
		super(gp);
		this.gp = gp;
		clock= 0; count = 0;
		speed = 1;
		area = new Rectangle(x + 8 ,y + 8, 32, 40);
		setLocation();
		way = "stand";
		getImage();
	}
	
	public void getImage() throws IOException {
		left1 = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/monster/redMleft1.png")));
		left2 = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/monster/redMleft2.png")));
		right1 = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/monster/redMright1.png")));
		right2  = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/monster/redMright2.png")));
		stand = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/monster/redMstanding.png")));
		dead1 = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/monster/redMdead1.png")));
		dead2 = ImageIO.read((new File("C:/Users/Aybars/eclipse-workspace/Homework/image/monster/redMdead2.png")));
	}
	
	public void run() {
		while (true) {
	        if (gp.gameState == gp.play) {
	            update();
	        }
	        try {
	            Thread.sleep(1000/60);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
    }
	
	public void update() {
		count++;
		if(count == 240) {			
	        int i = random.nextInt(100) + 1;
	        
	        if(i <= 25) {
	        	way = "up";
	        }
	        else if(i <= 50) {
	        	way = "down";
	        }
	        else if(i <= 75) {
	        	way = "left";
	        }
	        else if(i <= 100) {
	        	way = "right";
	        }
	        count=0;
		}
		int newX = x;
	    int newY = y;
		switch (way) {
        case "up":
           newY -= speed;
            break;
        case "down":
           newY += speed;
            break;
        case "left":
           newX -= speed;
            break;
        case "right":
           newX += speed;
            break;
		}
	
		
		if (!collisionWithWall(newX, newY) && !collisionWithBomb(newX, newY)) {
	        x = newX;
	        y = newY;
	        area.x = x + 8;
	        area.y = y + 8;
	    }
		if(collisionWithBomb(newX, newY)) {
			if(way.equals("down")) {
				way = "up";
				count = 240;
			}if(way.equals("up")) {
				way = "down";
				count = 240;
			}if(way.equals("right")) {
				way = "left";
				count = 240;
			}if(way.equals("left")) {
				way = "right";
				count = 240;
			}
		}
		
		clock++;
		if (clock > 30) {
			animation = !animation;
			clock = 0;
		}
    }
	
	public void paintComponent(Graphics2D g) {
		if(isAlive) {			
			switch(way) {
			case "up":
				if(animation) g.drawImage(right1, x - speed, y - speed, gp.tileSize, gp.tileSize, null);
				else g.drawImage(right2, x - speed, y - speed, gp.tileSize, gp.tileSize, null);
				break;
			case"down":
				if(animation) g.drawImage(left1, x, y + speed, gp.tileSize, gp.tileSize, null);
				else g.drawImage(left2, x - speed, y + speed, gp.tileSize, gp.tileSize, null);
				break;
			case "left":
				if (animation) g.drawImage(left1, x + speed, y, gp.tileSize, gp.tileSize, null);
				else g.drawImage(left2, x + speed, y, gp.tileSize, gp.tileSize, null);
				break;
			case"right":
				if(animation) g.drawImage(right1, x - speed, y, gp.tileSize, gp.tileSize, null);
				else g.drawImage(right2, x - speed, y, gp.tileSize, gp.tileSize, null);
				break;
			case "stand":
				g.drawImage(stand, x, y, gp.tileSize, gp.tileSize, null);
				break;
			}		
		}
			
	}
	

	public void setLocation() {
		int a = random.nextInt(30) * gp.tileSize; 
		int b = random.nextInt(10) * gp.tileSize;
		if(gp.manage.map[b/gp.tileSize][a/gp.tileSize] == 0 && b/gp.tileSize != 2 && b/gp.tileSize != 1 && a/gp.tileSize != 1 && a/gp.tileSize != 2) {
			x = a;
			y = b;
		}else {
			setLocation();
		}
	}
	
	public boolean collisionWithWall(int newX, int newY) {		
		int tileY = newY/gp.tileSize;
		int tileX = newX/gp.tileSize;
		
		if (gp.manage.isStrongWall(tileY, tileX) || gp.manage.isWeakWall(tileY, tileX) ||
		        gp.manage.isStrongWall(tileY, (newX + area.width) / gp.tileSize) || 
		        gp.manage.isWeakWall(tileY, (newX + area.width) / gp.tileSize) ||
		        gp.manage.isStrongWall((newY + area.height) / gp.tileSize, tileX) || 
		        gp.manage.isWeakWall((newY + area.height) / gp.tileSize, tileX) ||
		        gp.manage.isStrongWall((newY + area.height) / gp.tileSize, (newX + area.width) / gp.tileSize) || 
		        gp.manage.isWeakWall((newY + area.height) / gp.tileSize, (newX + area.width) / gp.tileSize)) {
		        return true;
		    }
		    return false;
	}
	
	public boolean collisionWithBomb(int newX, int newY) {
		int tileSize = gp.tileSize;

	    for (Bomb bomb : gp.bombs) {
	        int bombTileX = bomb.x / tileSize;
	        int bombTileY = bomb.y / tileSize;

	        int topLeftX = newX / tileSize;
	        int topLeftY = newY / tileSize;
	        int topRightX = (newX + area.width) / tileSize;
	        int topRightY = newY / tileSize;
	        int bottomLeftX = newX / tileSize;
	        int bottomLeftY = (newY + area.height) / tileSize;
	        int bottomRightX = (newX + area.width) / tileSize;
	        int bottomRightY = (newY + area.height) / tileSize;

	        if ((bombTileX == topLeftX && bombTileY == topLeftY) ||
	            (bombTileX == topRightX && bombTileY == topRightY) ||
	            (bombTileX == bottomLeftX && bombTileY == bottomLeftY) ||
	            (bombTileX == bottomRightX && bombTileY == bottomRightY)) {
	            return true;
	        }
	    }
	    return false;
	}
}
