package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Entity {

	GamePanel gp;
	public int x, y;
	public int speed;

	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, stand, dead1, dead2;
	public String way;

	public Rectangle area;
	public boolean canMove;

	public Entity(GamePanel gp) {
		this.gp = gp;
	}
}
