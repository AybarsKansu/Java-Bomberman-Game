package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import main.GamePanel;

public class SaveLoad {
	GamePanel gp;
	File file = new File("save.dat");

	public SaveLoad(GamePanel gp) {
		this.gp = gp;
	}

	public void save() {
		try {

			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			Data data = new Data();

			data.playerX = gp.player.x;
			data.playerY = gp.player.y;
			data.health = GamePanel.health;
			data.time = gp.countdown;
			data.map = gp.manage.map;
			data.doorX = gp.manage.doorX;
			data.doorY = gp.manage.doorY;
			data.powerX = gp.power.x;
			data.powerY = gp.power.y;
			for (int i = 0; i < gp.monsters.size(); i++) {
				ArrayList<Integer> temp = new ArrayList<>();
				temp.add(gp.monsters.get(i).x);
				temp.add(gp.monsters.get(i).y);
				data.redM.add(temp);
			}

			out.writeObject(data);

		} catch (FileNotFoundException e) {
			System.out.println("Dosya yok");
		} catch (IOException e) {
			System.out.println("Yazmada hata");
		}
	}

	public void load() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			Data data = (Data) in.readObject();

			gp.player.x = data.playerX;
			gp.player.y = data.playerY;
			GamePanel.health = data.health;
			gp.countdown = data.time;
			gp.manage.map = data.map;
			gp.manage.doorX = data.doorX;
			gp.manage.doorY = data.doorY;
			gp.power.x = data.powerX;
			gp.power.y = data.powerY;

			for (int i = 0; i < gp.monsters.size(); i++) {
				gp.monsters.get(i).x = data.redM.get(i).get(0);
				gp.monsters.get(i).y = data.redM.get(i).get(1);
			}

		} catch (Exception e) {
			System.out.println("Okuma");
		}
	}
}
