package main;

import entity.Entity;
import entity.Player;

public class Collusion {
	GamePanel gp;
	int x = 0, y = 0, z = 0, t = 0;
	public boolean powerImplement = false;

	public Collusion(GamePanel gp) {
		this.gp = gp;
	}

	public void checkCollision() {
		int leftX = gp.player.area.x;
		int rightX = gp.player.area.x + gp.player.area.width;
		int topY = gp.player.area.y;
		int bottomY = gp.player.area.y + gp.player.area.height;

		int leftTile = leftX / gp.tileSize;
		int rightTile = rightX / gp.tileSize;
		int topTile = topY / gp.tileSize;
		int bottomTile = bottomY / gp.tileSize;

		switch (gp.player.way) {
			case "up":
				topTile = (topY - gp.player.speed) / gp.tileSize;
				if ((gp.manage.isWeakWall(topTile, leftTile) && !powerImplement)
						|| (gp.manage.isWeakWall(topTile, rightTile) && !powerImplement)
						|| gp.manage.isStrongWall(topTile, rightTile) || gp.manage.isStrongWall(topTile, leftTile)) {
					gp.player.canMove = false;
				}

				break;
			case "down":
				bottomTile = (bottomY + gp.player.speed) / gp.tileSize;
				if ((gp.manage.isWeakWall(bottomTile, leftTile) && !powerImplement)
						|| (gp.manage.isWeakWall(bottomTile, rightTile) && !powerImplement)
						|| gp.manage.isStrongWall(bottomTile, rightTile) || gp.manage.isStrongWall(bottomTile, leftTile)) {
					gp.player.canMove = false;
				}

				break;
			case "left":
				leftTile = (leftX - gp.player.speed) / gp.tileSize;
				if ((gp.manage.isWeakWall(topTile, leftTile) && !powerImplement)
						|| (gp.manage.isWeakWall(bottomTile, leftTile) && !powerImplement)
						|| gp.manage.isStrongWall(bottomTile, leftTile) || gp.manage.isStrongWall(topTile, leftTile)) {
					gp.player.canMove = false;
				}

				break;
			case "right":
				rightTile = (rightX + gp.player.speed) / gp.tileSize;
				if ((gp.manage.isWeakWall(topTile, rightTile) && !powerImplement)
						|| (gp.manage.isWeakWall(bottomTile, rightTile) && !powerImplement)
						|| (gp.manage.isStrongWall(topTile, rightTile) || (gp.manage.isStrongWall(bottomTile, rightTile)))) {
					gp.player.canMove = false;
				}
				break;
		}

	}
}
