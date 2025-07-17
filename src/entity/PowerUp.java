package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import object.Bomb;
import object.Explosion;

public class PowerUp extends Entity {
	
	GamePanel gp;
	public BufferedImage yellow;
	public BufferedImage green;
	public BufferedImage blue;
	public BufferedImage red;
	public BufferedImage cyan;
	public boolean chooseCharacter = false;
	public static int count = 0;
	
	public PowerUp(GamePanel gp) throws IOException {
		super(gp);
		this.gp = gp;
		x = 0;
		y = 0;
		getImage();
	}
	
	public void getImage() throws IOException {
		BufferedImage image = ImageIO.read(new File("image/object/powers.png"));
		yellow = image.getSubimage(0, 0, gp.originalTileSize, gp.originalTileSize); // bomb number expansion
        green = image.getSubimage(gp.originalTileSize, 0, gp.originalTileSize, gp.originalTileSize); // bomb radius expansion
        blue = image.getSubimage(gp.originalTileSize * 2, 0, gp.originalTileSize, gp.originalTileSize); // weak wall passing
        red = image.getSubimage(gp.originalTileSize * 3, 0, gp.originalTileSize, gp.originalTileSize); // increase speed
        cyan = image.getSubimage(gp.originalTileSize * 4, 0, gp.originalTileSize, gp.originalTileSize); // bomb time 
	}
	
	public void paint(Graphics g) {		
		
			if(gp.player.powerImage) {	
				switch (gp.powerSelect) {
				case 0:
					g.drawImage(yellow, x, y, gp.tileSize, gp.tileSize, null);
					break;
				case 1:
					g.drawImage(green, x, y, gp.tileSize, gp.tileSize, null);
					break;
				case 2:
					g.drawImage(blue, x, y, gp.tileSize, gp.tileSize, null);
					break;
				case 3:
					g.drawImage(red, x, y, gp.tileSize, gp.tileSize, null);
					break;
				case 4:
					g.drawImage(cyan, x, y, gp.tileSize, gp.tileSize, null);
					break;
			}
		}
}

	public void placePower(int x, int y) {
        this.x = x * gp.tileSize;
        this.y = y * gp.tileSize;
        gp.manage.powerPlaced = true;
    }
	
	public void changeCharacter() {
		if(gp.manage.powerFind) {
			chooseCharacter = true;
		}else {
			chooseCharacter = false;
		}
	}
	
	public void bombNumberExpansion() {
		GamePanel.maxBombs = 2;
	}
	
	public void bombRadiusExpansion() {
		Explosion.radius = 2;
	}
	
	public void increaseSpeed() {
		gp.player.speed = 5;
	}
	
	public void allowWeakWallPassing() {
		gp.bump.powerImplement = true;
	}
	
	public void giveBombControl() {
		Bomb.powerImplement = true;
		Bomb.pressedB = 2;
	}
}
