package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

import object.Bomb;
import object.Explosion;

public class KeyHandler implements KeyListener {
	public GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed;
	public boolean bombDrop = false;
	public boolean newGame = false;
	public boolean changeCharacter = false;
	int a = 0;

	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (gp.gameState == gp.title) {
			if (code == KeyEvent.VK_UP) {
				if (gp.command != 0)
					gp.command--;
				else
					gp.command = 0;
			}
			if (code == KeyEvent.VK_DOWN) {
				if (gp.command != 4)
					gp.command++;
				else
					gp.command = 4;
			}
			if (code == KeyEvent.VK_ENTER) {

				if (gp.command == 0) {
					resetGame();
				}
				if (gp.command == 1) {
					gp.gameState = gp.play;
				}
				if (gp.command == 2) {
					resetGame();
					gp.inout.load();
					gp.gameState = gp.play;
				}
				if (gp.command == 3) {
					gp.gameState = gp.settings;
				}
				if (gp.command == 4) {
					System.exit(0);
				}
			}
		}

		else if (gp.gameState == gp.play) {
			switch (code) {
				case KeyEvent.VK_UP:
					upPressed = true;
					break;
				case KeyEvent.VK_DOWN:
					downPressed = true;
					break;
				case KeyEvent.VK_LEFT:
					leftPressed = true;
					break;
				case KeyEvent.VK_RIGHT:
					rightPressed = true;
					break;
				case KeyEvent.VK_Z:
					bombDrop = true;
					break;
				case KeyEvent.VK_P:
					gp.gameState = gp.stop;
					break;
				case KeyEvent.VK_ESCAPE:
					gp.gameState = gp.title;
					break;
				case KeyEvent.VK_S:
					gp.inout.save();
					break;
				case KeyEvent.VK_B:
					Bomb.pressedB = 1;
					break;
			}
		}

		else if (gp.gameState == gp.settings) {
			if (code == KeyEvent.VK_UP) {
				if (gp.set != 0)
					gp.set--;
				else
					gp.set = 0;
			}
			if (code == KeyEvent.VK_DOWN) {
				if (gp.set != 7)
					gp.set++;
				else
					gp.set = 7;
			}
			if (code == KeyEvent.VK_RIGHT) {
				if (gp.set == 5) {
					if (gp.musicVolume < 100)
						gp.musicVolume += 10;
					gp.stopMusic();
					gp.playMusic(0);
				} else if (gp.set == 6) {
					if (gp.player.imageOrder != 3)
						gp.player.imageOrder++;
					else
						gp.player.imageOrder = 0;
				} else {
					gp.set = 5;
				}
			}
			if (code == KeyEvent.VK_LEFT) {
				if (gp.set == 5) {
					if (gp.musicVolume > 0)
						gp.musicVolume -= 10;
					gp.stopMusic();
					gp.playMusic(0);
				} else if (gp.set == 6) {
					if (gp.player.imageOrder != 0)
						gp.player.imageOrder--;
					else
						gp.player.imageOrder = 3;
				}
			}
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.title;
			}
			if (code == KeyEvent.VK_ENTER) {
				switch (gp.set) {
					case 0:
						gp.powerSelect = 0;
						gp.gameState = gp.title;
						break;
					case 1:
						gp.powerSelect = 1;
						gp.gameState = gp.title;
						break;
					case 2:
						gp.powerSelect = 2;
						gp.gameState = gp.title;
						break;
					case 3:
						gp.powerSelect = 3;
						gp.gameState = gp.title;
						break;
					case 4:
						gp.powerSelect = 4;
						gp.gameState = gp.title;
						break;
					case 6:
						changeCharacter = true;
						gp.gameState = gp.title;
						break;
				}
			}
		}

		else if (gp.gameState == gp.stop) {
			if (code == KeyEvent.VK_P) {
				gp.gameState = gp.play;
			}
		}

		else if (gp.gameState == gp.gameOver) {
			if (code == KeyEvent.VK_UP) {
				gp.over = 0;
			}

			if (code == KeyEvent.VK_DOWN) {
				gp.over = 1;
			}

			if (code == KeyEvent.VK_ENTER) {
				if (gp.over == 1) {
					System.exit(0);
				} else {
					gp.player.isAlive = true;
					GamePanel.health = 2;
					gp.player.x = gp.tileSize;
					gp.player.y = gp.tileSize;
					gp.gameState = gp.play;
				}
			}
		} else if (gp.gameState == gp.won) {
			if (code == KeyEvent.VK_UP) {
				gp.index = 1;
			}
			if (code == KeyEvent.VK_DOWN) {
				gp.index = 0;
			}
			if (code == KeyEvent.VK_ENTER) {
				if (gp.index == 1) {
					GamePanel.health++;
					resetGame();
				}
				if (gp.index == 0)
					gp.gameState = gp.title;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		switch (code) {
			case 38:
				upPressed = false;
				break;
			case 40:
				downPressed = false;
				break;
			case 37:
				leftPressed = false;
				break;
			case 39:
				rightPressed = false;
				break;
			case KeyEvent.VK_Z:
				bombDrop = false;
				if (gp != null) {
					gp.placeBomb();
				}
				break;
			case KeyEvent.VK_B:
				Bomb.pressedB = 2;
				break;
		}
	}

	public void resetGame() {
		gp.player.x = gp.tileSize;
		gp.player.y = gp.tileSize;
		gp.manage.resetMap();
		gp.manage.random();
		GamePanel.health = 2;
		gp.countdown = 200;
		gp.manage.powerFind = false;
		if (gp.monsters != null)
			gp.monsters.clear();
		if (gp.bmonsters != null)
			gp.bmonsters.clear();
		if (gp.bombs != null)
			gp.bombs.clear();
		try {
			GamePanel.countMonsters = 0;
			gp.spawnMonsters();
			gp.update();
			gp.repaint();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		gp.gameState = gp.play;
	}
}
