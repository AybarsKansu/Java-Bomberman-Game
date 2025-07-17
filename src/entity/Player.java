package entity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.GamePanel;
import main.KeyHandler;
import object.Bomb;

public class Player extends Entity {

	GamePanel gp;
	KeyHandler keyH;
	
	int bombCollisionCount = 0; 
	boolean animation;
	public boolean isAlive;
	int clock;
	public String power;
	public boolean powerImage;
	public int imageOrder=0;

	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		this.gp = gp;
		this.keyH = keyH;
		isAlive = true;
		powerImage = true;
		x = gp.tileSize;
		y = gp.tileSize;
		speed = 3;
		way = "stand";	
		animation = true;		
		clock = 0;
		area = new Rectangle(x, y, 24, 32);
		getImage();
	}

	public void getImage() {
		try {
			if(imageOrder == 0) {
				up1 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerup1.png"));
				up2 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerup2.png"));
				down1 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerdown1.png"));
				down2 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerdown2.png"));
				left1 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerleft1.png"));
				left2 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerleft2.png"));
				right1 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerright1.png"));
				right2 = ImageIO.read(getClass().getResourceAsStream("/karakter/playerright2.png"));
				stand = ImageIO.read(getClass().getResourceAsStream("/karakter/playerstand1.png"));
			}else if(imageOrder == 1) {
				up1 = ImageIO.read(new File("image/karakter/boy_up_1.png"));
				up2 = ImageIO.read(new File("image/karakter/boy_up_2.png"));
				down1 = ImageIO.read(new File("image/karakter/boy_down_1.png"));
				down2 = ImageIO.read(new File("image/karakter/boy_down_2.png"));
				left1 = ImageIO.read(new File("image/karakter/boy_left_1.png"));
				left2 = ImageIO.read(new File("image/karakter/boy_left_2.png"));
				right1 = ImageIO.read(new File("image/karakter/boy_right_1.png"));
				right2 = ImageIO.read(new File("image/karakter/boy_right_2.png"));
				stand = ImageIO.read(new File("image/karakter/boy_down_1.png"));
			}else if(imageOrder == 2) {
				up1 = ImageIO.read(new File("image/karakter/girldown1.png"));
				up2 = ImageIO.read(new File("image/karakter/girldown2.png"));
				down1 = ImageIO.read(new File("image/karakter/girldown1.png"));
				down2 = ImageIO.read(new File("image/karakter/girldown2.png"));
				left1 = ImageIO.read(new File("image/karakter/girlleft1.png"));
				left2 = ImageIO.read(new File("image/karakter/girlleft2.png"));
				right1 = ImageIO.read(new File("image/karakter/girlright1.png"));
				right2 = ImageIO.read(new File("image/karakter/girlright2.png"));
				stand = ImageIO.read(new File("image/karakter/girldown1.png"));
			}else if(imageOrder == 3) {
				up1 = ImageIO.read(new File("image/karakter/nup1.png"));
				up2 = ImageIO.read(new File("image/karakter/nup2.png"));
				down1 = ImageIO.read(new File("image/karakter/nright1.png"));
				down2 = ImageIO.read(new File("image/karakter/nright2.png"));
				left1 = ImageIO.read(new File("image/karakter/nleft1.png"));
				left2 = ImageIO.read(new File("image/karakter/nleft2.png"));
				right1 = ImageIO.read(new File("image/karakter/nright1.png"));
				right2 = ImageIO.read(new File("image/karakter/nright2.png"));
				stand = ImageIO.read(new File("image/karakter/nup1.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
					
		if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
			
			if (keyH.upPressed) {				
				way = "up";				
			}

			if (keyH.downPressed) {				
				way = "down";					
			}

			if (keyH.leftPressed) {
				way = "left";				
			}

			if (keyH.rightPressed) {
				way = "right";				
			}
			canMove = true;
			gp.bump.checkCollision();
			
			if(canMove) {
				switch (way) {
				case "up":
					y -= speed;
					break;
				case"down":
					y += speed;
					break;
				case"left":
					x -=  speed;
					break;
				case"right":
					x += speed; 
					break;
				}

			}
			clock++;
			if (clock > 15) {
				animation = !animation;
				clock = 0;
			}
		}
		area.x = x + 12;
		area.y = y + 4;
		
		checkMonsterCollision();
		checkForPower();
		isGameOver();
	}

	public void paintComponent(Graphics2D g2) {
		BufferedImage player = null;		
		switch (way) {
		case "up":
			if (animation)
				player = up1;
			else
				player = up2;
			break;
		case "down":
			if (animation)
				player = down1;
			else
				player = down2;
			break;
		case "left":
			if (animation)
				player = left1;
			else
				player = left2;
			break;
		case "right":
			if (animation)
				player = right1;
			else
				player = right2;
			break;
		default:
			player = stand;
			break;
		}
			g2.drawImage(player, x, y, gp.tileSize, gp.tileSize, null);
			if(gp.keyHandler.changeCharacter == true) {	
				if(powerImage)
		        	gp.power.paint(g2);
				getImage();
				gp.keyHandler.changeCharacter = false;
			}			
	}
	
	public void resetBombCollisionCount() {
        bombCollisionCount = 0;
    }
	
	public boolean collisionWithBomb() {
	    for (Bomb bomb : gp.bombs) {
	        if (x / gp.tileSize == bomb.x / gp.tileSize && y / gp.tileSize == bomb.y / gp.tileSize) {
	            if(bombCollisionCount == 0) {
	            	canMove = true;
	            	bombCollisionCount++;
	            	return false;
	            }else {
	            	canMove = false;
	        		return true;
	            }
	        }else {
	        	canMove = true;
	        }
	    }
	    return false;
	}
	
	public void isGameOver() {
		if(!isAlive) {
			if(GamePanel.health > 0) {
				GamePanel.health--;
				x = gp.tileSize;
				y = gp.tileSize;
				isAlive = true;
			} else {
				gp.gameState = gp.gameOver;
			}
		}else {
			if(Math.abs(x - gp.manage.doorX) < 10 && Math.abs(y - gp.manage.doorY) < 10 && GamePanel.countMonsters == 0) {
				gp.gameState = gp.won;
			}
		}
	}
	
	public void checkMonsterCollision() {	
		for(int i = 0; i < gp.monsters.size(); i++) {		
			if(gp.monsters.get(i).area.intersects(this.area)) {
				isAlive = false; // change later
			}
		}
		for(int i = 0; i < gp.bmonsters.size(); i++) {		
			if(gp.bmonsters.get(i).area.intersects(this.area)) {
				isAlive = false; // change later
			}
		}
	}
	
	public void checkForPower() {
		if(Math.abs(gp.power.x - x) < 20 && Math.abs(gp.power.y - y) < 20 && gp.manage.powerFind) {
			powerImage = false;
			switch (gp.powerSelect) {
			case 1:
				gp.power.bombNumberExpansion();
				break;
			case 2:
				gp.power.bombRadiusExpansion();
				break;
			case 3:
				gp.power.allowWeakWallPassing();
				break;
			case 4:
				gp.power.increaseSpeed();
				break;
			case 0:
				gp.power.giveBombControl();
				break;
			}
		}
	}
	
}