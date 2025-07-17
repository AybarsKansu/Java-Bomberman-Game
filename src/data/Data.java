package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Data implements Serializable {
	int playerX, playerY;
	int health;
	int time;
	int[][] map;
	int doorX, doorY;
	int powerX, powerY;
	ArrayList<ArrayList<Integer>> redM = new ArrayList<>();
	ArrayList<ArrayList<Integer>> blueM = new ArrayList<>();
}
