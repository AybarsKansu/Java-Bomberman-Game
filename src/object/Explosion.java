
package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Explosion {
	
	GamePanel gp;
	int x, y; public static int radius = 1;
	BufferedImage imageup;
	BufferedImage imageleft;
	BufferedImage imagecenter;
	long startTime;
    public int duration = 500;
    public Rectangle hitbox;
    public static int hit = 0;
    public static int hitp = 0;
    
	public Explosion(GamePanel gp, int x, int y) {
		this.gp = gp;
		this.x = x;
		this.y = y;
		hitbox = new Rectangle(x - 8, y - 8, 40, 40);
		this.startTime = System.currentTimeMillis();
		getImage();
	}
	
	public void getImage() {
        try {
            imageup = ImageIO.read(new File("C:/Users/Aybars/eclipse-workspace/Homework/image/object/explosionup.png"));
            imageleft = ImageIO.read(new File("C:/Users/Aybars/eclipse-workspace/Homework/image/object/explosionleft.png"));
            imagecenter = ImageIO.read(new File("C:/Users/Aybars/eclipse-workspace/Homework/image/object/explosioncenter.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration;
    }
	
	public void handleExplosion() throws IOException {
        int row = y / gp.tileSize;
        int col = x / gp.tileSize;
        gp.soundEffect(1);
        for (int r = 1; r <= radius; r++) {	
            if(r == 2) {
            	if(!gp.manage.isStrongWall(row - 1, col)){
                	killPlayer(row - r, col);
                	killMonster(row - r, col);
                	collisionWithDoor(row - r, col);
                	collisionWithPower(row - r, col);
                	destroyWall(row - r, col);
                }
                if(!gp.manage.isStrongWall(row + 1, col)) {
                	killPlayer(row + r, col);
                	killMonster(row + r, col);
                	collisionWithDoor(row + r, col);
                	collisionWithPower(row + r, col);
                	destroyWall(row + r, col);
                }
                if(!gp.manage.isStrongWall(row, col - 1)) {
                	killPlayer(row, col - r);
                	killMonster(row, col - r);
                	collisionWithDoor(row, col - r);
                	collisionWithPower(row, col - r);
                	destroyWall(row, col - r);
                }
                if(!gp.manage.isStrongWall(row, col + 1)) {
                	killPlayer(row, col + r);
                	killMonster(row, col + r);
                	collisionWithDoor(row, col + r);
                	collisionWithPower(row, col + r);
                	destroyWall(row, col + r);
                }
                destroyWall(row, col);
            }else {
	            killPlayer(row - r, col);
	            killPlayer(row + r, col);
	            killPlayer(row, col - r);
	            killPlayer(row, col + r);
	            killPlayer(row, col);
	            killMonster(row - r, col);
	            killMonster(row + r, col);
	            killMonster(row, col - r);
	            killMonster(row, col + r);
	            killMonster(row, col);
	            collisionWithDoor(row - r, col);
	            collisionWithDoor(row + r, col);
	            collisionWithDoor(row, col - r);
	            collisionWithDoor(row, col + r);
	            collisionWithDoor(row, col);
	            collisionWithPower(row - r, col);
	            collisionWithPower(row + r, col);
	            collisionWithPower(row, col - r);
	            collisionWithPower(row, col + r);
	            collisionWithPower(row, col);
	            destroyWall(row - r, col);
	            destroyWall(row + r, col);
	            destroyWall(row, col - r);
	            destroyWall(row, col + r);
	            destroyWall(row, col);
            }
        }
    }

    private void destroyWall(int row, int col) {
        if(row >= 0 && col >= 0 && row < gp.maxRow && col < gp.maxCol) {
	    	if (!gp.manage.isStrongWall(row, col)) { 
	            if (gp.manage.isWeakWall(row, col)) {
	                gp.manage.destroyWeakWall(row, col);
	            }
	        }
        }
    }
	
    public void killPlayer(int row, int col) {
    	if(row >= 0 && col >= 0 && row < gp.maxRow && col < gp.maxCol) {
	    	if(gp.player.x / gp.tileSize == col && gp.player.y / gp.tileSize == row) {
	    		gp.player.isAlive = false; // change later
	    	}
    	}
 
    }
    
    public void killMonster(int row, int col) {
    	if(row >= 0 && col >= 0 && row < gp.maxRow && col < gp.maxCol) {
	    	for(int i = 0; i < gp.monsters.size(); i++) {
	    		if(gp.monsters.get(i).x / gp.tileSize == col && gp.monsters.get(i).y / gp.tileSize == row) {
	    			gp.monsters.get(i).isAlive = false;    			
	    			gp.monsters.remove(i);
	    			GamePanel.countMonsters--;
	    			GamePanel.points += 100;
	    		}
	    	}
    	}
    	if(row >= 0 && col >= 0 && row < gp.maxRow && col < gp.maxCol) {
	    	for(int i = 0; i < gp.bmonsters.size(); i++) {
	    		if(gp.bmonsters.get(i).x / gp.tileSize == col && gp.bmonsters.get(i).y / gp.tileSize == row) {
	    			gp.bmonsters.get(i).isAlive = false;    			
	    			gp.bmonsters.remove(i);
	    			GamePanel.countMonsters--;
	    			GamePanel.points += 200;
	    		}
	    	}
    	}
    }
    
    public void collisionWithDoor(int row, int col) throws IOException {
    	if (gp.manage.doorX / gp.tileSize == col && gp.manage.doorY / gp.tileSize == row){
    		hit++;
    		if(hit > 1) {
    			gp.spawnBlueMonsters();
    		}
    	}
    }
       
    public void collisionWithPower(int row, int col) throws IOException {
        if(gp.power.x / gp.tileSize == col && gp.power.y / gp.tileSize == row) {
            hitp++;
        	if(hitp > 1) {
        		if(gp.player.powerImage == true){
        			gp.spawnBlueMonsters();       	
            }else {
            	gp.manage.powerFind = true;
            	}
        	}
        }
    }
    
	public void paint(Graphics2D g2) {
        int row = y / gp.tileSize;
        int col = x / gp.tileSize;       
        
        g2.drawImage(imagecenter, x, y, gp.tileSize, gp.tileSize, null);

        for (int r = 1; r <= radius; r++) {
            if(r == 2) {
            	if(!gp.manage.isStrongWall(row - 1, col)) paintExplosion(g2, row - r, col, x, y - r * gp.tileSize, imageup);
            	if(!gp.manage.isStrongWall(row + 1, col)) paintExplosion(g2, row + r, col, x, y + r * gp.tileSize, imageup);
            	if(!gp.manage.isStrongWall(row, col - 1)) paintExplosion(g2, row, col - r, x - r * gp.tileSize, y, imageleft);
            	if(!gp.manage.isStrongWall(row, col + 1)) paintExplosion(g2, row, col + r, x + r * gp.tileSize, y, imageleft);
            }else {
            	paintExplosion(g2, row - r, col, x, y - r * gp.tileSize, imageup);
            	paintExplosion(g2, row + r, col, x, y + r * gp.tileSize, imageup);
            	paintExplosion(g2, row, col - r, x - r * gp.tileSize, y, imageleft);
            	paintExplosion(g2, row, col + r, x + r * gp.tileSize, y, imageleft);
            }
        }

        
    }

    private void paintExplosion(Graphics2D g2, int row, int col, int x, int y, BufferedImage image) {
    	if(row >= 0 && col >= 0 && row < gp.maxRow && col < gp.maxCol) {
	    	if (!gp.manage.isStrongWall(row, col)) {
	            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	            if (gp.manage.doorX / gp.tileSize == col && gp.manage.doorY / gp.tileSize == row) {
	                gp.manage.map[row][col] = 3;
	                gp.manage.doorFind = true;
	            } else if(gp.power.x / gp.tileSize == col && gp.power.y / gp.tileSize == row) {
	            	gp.manage.powerFind = true;
	            } else {
	                gp.manage.map[row][col] = 0;
	            }
	        }
    	}
    }
    
}

