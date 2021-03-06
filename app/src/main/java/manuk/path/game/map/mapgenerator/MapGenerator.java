package manuk.path.game.map.mapgenerator;

import manuk.path.game.util.LList;

import java.util.Random;

public abstract class MapGenerator {
	private static Random random = new Random();
	public int[][][] map;
	public Pos spawn;
	public LList<Pos3> enemySpawn;
	public LList<Pos> shrineSpawn;
	
	MapGenerator() {
		enemySpawn = new LList<>();
		shrineSpawn = new LList<>();
	}
	
	public abstract int[][][] generate(int width, int length, int height);
	
	static int randInt(int min, int max) {
		return random.nextInt(max - min) + min;
	}
	
	static boolean randFlip() {
		return random.nextBoolean();
	}
	
	static boolean randFlip(double weight) {
		return random.nextDouble() < weight;
	}
	
	public static class Pos {
		public int x, y;
		
		Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static class Pos3 {
		public int x, y, z;
		
		Pos3(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}