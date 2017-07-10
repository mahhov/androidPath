package manuk.path.game.util;

import manuk.path.game.map.Map;
import manuk.path.game.map.MapEntity;

public class IntersectionFinder {
	private Map map;
	private long id;
	private boolean allowSlide, limitDistance;
	private double x, y;
	private double nextx, nexty;
	private int intx, inty;
	private double movex, movey, move, moved, maxMove;
	private int moveWhich;
	private double[] dir;
	private int layer;
	private double size;
	private int entityStartX, entityEndX, entityStartY, entityEndY;
	public MapEntity entityCollide;
	private double entityDist;
	private double entityCross, entityDot, entityPyth;
	private double entityMove;
	private int collideNum, isDirZeroNum;
	private boolean[] collide, isDirZero;
	private int xx, yy, ll; // for iterating
	
	public IntersectionFinder(Map map) {
		this.map = map;
	}
	
	// return r[3]: 1 if map collision, 0 if maxMove, 2 if entity collision
	public double[] find(long id, double[] orig, double[] dir, double maxMove, boolean allowSlide, int layer, double size) {
		reset(id, orig, dir, maxMove, allowSlide, layer, size);
		if (isDirZeroNum == 2)
			return new double[] {orig[0], orig[1], 1};
		while (true) {
			computeNextMove();
			moveBy(move - Math3D.EPSILON);
			entityCollideCheck();
			if (moved + move > maxMove && limitDistance) {
				moveBy(maxMove - moved);
				return new double[] {nextx, nexty, 0};
			} else if (entityCollide != null) {
				moveBy(move - Math3D.EPSILON);
				return new double[] {nextx, nexty, 2, entityCollide.mapX, entityCollide.mapY, entityCollide.layer};
			}
			moveBy(move + Math3D.EPSILON);
			if (!map.isMoveable(intx, inty, 0)) {
				moveBy(move - Math3D.EPSILON);
				if (collideCheck())
					return new double[] {x, y, 1};
			}
			nextIter();
		}
	}
	
	private void reset(long id, double[] orig, double[] dir, double maxMove, boolean allowSlide, int layer, double size) {
		this.id = id;
		x = nextx = orig[0];
		y = nexty = orig[1];
		intx = (int) x;
		inty = (int) y;
		moved = 0;
		this.maxMove = maxMove;
		this.limitDistance = maxMove > 0;
		this.dir = Math3D.setMagnitude(dir[0], dir[1], 1);
		this.layer = layer;
		this.size = size;
		this.allowSlide = allowSlide;
		collideNum = 0;
		isDirZeroNum = 0;
		collide = new boolean[] {false, false};
		isDirZero = new boolean[] {Math3D.isZero(dir[0]), Math3D.isZero(dir[1])};
		if (isDirZero[0]) {
			dir[0] = 0;
			isDirZeroNum++;
		}
		if (isDirZero[1]) {
			dir[1] = 0;
			isDirZeroNum++;
		}
	}
	
	private void computeNextMove() {
		if (isDirZero[0])
			movex = Math3D.sqrt3;
		else {
			if (dir[0] > 0)
				movex = (1 + intx - x) / dir[0];
			else {
				movex = (intx - x) / dir[0];
			}
		}
		
		if (isDirZero[1])
			movey = Math3D.sqrt3;
		else {
			if (dir[1] > 0)
				movey = (1 + inty - y) / dir[1];
			else
				movey = (inty - y) / dir[1];
		}
		
		if (movex < movey) {
			moveWhich = 0;
			move = movex;
		} else {
			moveWhich = 1;
			move = movey;
		}
	}
	
	private void entityCollideCheck() {
		entityDist = MapEntity.MAX_ENTITY_SIZE + size + move;
		entityCollide = null;
		entityStartX = (int) Math3D.max(nextx - entityDist, 0);
		entityStartY = (int) Math3D.max(nexty - entityDist, 0);
		entityEndX = (int) Math3D.min(nextx + entityDist, map.width - 1);
		entityEndY = (int) Math3D.min(nexty + entityDist, map.length - 1);
		for (ll = 0; ll < MapEntity.ENTITY_LAYERS_COUNT; ll++)
			if (MapEntity.ENTITY_COLLISION[layer][ll])
				for (xx = entityStartX; xx <= entityEndX; xx++)
					for (yy = entityStartY; yy <= entityEndY; yy++)
						for (MapEntity entity : map.entity[xx][yy][ll])
							if (entity.id != id && entity.size + size + move > Math3D.magnitude(nextx - entity.mapX, nexty - entity.mapY)) { // if possible intersection
								entityCross = Math3D.cross(x - entity.mapX, y - entity.mapY, dir[0], dir[1]);
								entityDot = Math3D.dot(entity.mapX - x, entity.mapY - y, dir[0], dir[1]);
								entityPyth = Math3D.pythagorean(entity.size + size, entityCross);
								if (entityDot < 0) // if moving away
									break;
								entityMove = entityDot - entityPyth;
								if (entityMove < 0) { // if already inside
									move = 0;
									entityCollide = entity;
									return;
								}
								if (entityMove < move) { // if going to intersect
									move = entityMove;
									entityCollide = entity;
								}
							}
	}
	
	private void moveBy(double move) {
		nextx = x + dir[0] * move;
		nexty = y + dir[1] * move;
		
		intx = (int) nextx;
		inty = (int) nexty;
		if (nextx < 0)
			intx--;
		if (nexty < 0)
			inty--;
	}
	
	private boolean collideCheck() {
		if (!allowSlide)
			return true;
		if (!collide[moveWhich]) {
			collideNum++;
			collide[moveWhich] = true;
			if (!isDirZero[moveWhich]) {
				isDirZeroNum++;
				isDirZero[moveWhich] = true;
			}
			dir[moveWhich] = 0;
			return collideNum == 2 || isDirZeroNum == 2;
		}
		return false;
	}
	
	private void nextIter() {
		moved += move;
		x = nextx;
		y = nexty;
	}
}
