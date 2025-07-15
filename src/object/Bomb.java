package object;

import java.awt.Graphics2D;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import main.GamePanel;

public class Bomb{

    GamePanel gp;
    public BufferedImage image, image2, image3;
	public String name;
	public boolean collision;
    public int x, y;
    public int timer = 3000;
    public boolean exploded = false;
    public long elapsedTime = 0;
    public long placementTime;
    public Explosion explosion;
    public static boolean powerImplement = false;
    public static int pressedB = 0;
    static int imageCount = 0;
    
    public Bomb(GamePanel gp, int x , int y) {
        this.gp = gp;
        this.x = x; this.y = y;
        this.placementTime = System.currentTimeMillis();
        getImage();        
    }

    public void getImage() {
        try {
            image = ImageIO.read(new File("C:/Users/Aybars/eclipse-workspace/Homework/image/object/bomb1.png"));
            image2 = ImageIO.read(new File("C:/Users/Aybars/eclipse-workspace/Homework/image/object/bomb2.png"));
            image3 = ImageIO.read(new File("C:/Users/Aybars/eclipse-workspace/Homework/image/object/bomb3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void paint(Graphics2D g2) {
    	if (exploded && explosion != null && !explosion.isExpired()) {
            explosion.paint(g2);
        } else if (!exploded) {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
            
            if(imageCount < 30) {
            	g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
            }
            else if(imageCount >= 30 && imageCount < 60) {
            	g2.drawImage(image2, x, y, gp.tileSize, gp.tileSize, null);
            }
            else if(imageCount >= 60) {
            	g2.drawImage(image3, x, y, gp.tileSize, gp.tileSize, null);
            }
        }       
    }   
    
    public void update() throws IOException {
     
    	if (isExploded()) {
            if (!exploded) {       	
                explosion = new Explosion(gp, x, y);
                exploded = true;
                explosion.handleExplosion();
                GamePanel.currentBombs--;
            }             
        }
    	if(imageCount < 90) {
    		imageCount++;
    	}else {
    		imageCount = 0;
    	}
    }
    
    public boolean isExploded() {
    	if(!powerImplement || pressedB == 0)
    		return System.currentTimeMillis() - placementTime > timer;
    	else    		
    		return pressedB == 1;
    }
   
    public Rectangle getHitbox() {
        return new Rectangle(x*gp.tileSize, y*gp.tileSize, gp.tileSize, gp.tileSize);
    }
}