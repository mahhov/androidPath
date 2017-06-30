package manuk.path.game;

import manuk.path.game.mapgenerator.MapGenerator;
import manuk.path.game.util.Math3D;

class Character {
	private double speed = .5;
	double x, y;
	private double goalX, goalY;
	
	Character(MapGenerator mapGenerator) {
		goalX = x = mapGenerator.startX;
		goalY = y = mapGenerator.startY;
	}
	
	void move(Controller controller, Map map) {
		if (controller.isDown()) {
			goalX = controller.touchX * Engine.VIEW_WIDTH + map.scrollX;
			goalY = controller.touchY * Engine.VIEW_HEIGHT + map.scrollY;
		}
		
		double deltaX = goalX - x, deltaY = goalY - y;
		double dist = Math3D.min(speed, Math3D.magnitude(deltaX, deltaY));
		double[] intersection = map.intersectionFinder.find(new double[] {x, y}, new double[] {deltaX, deltaY}, dist, true);
		x = intersection[0];
		y = intersection[1];
	}
	
	void draw(double scrollX, double scrollY) {
		boolean[] side = new boolean[] {true, true, true, true, true, true};
		MapPainter.drawBlock(x - scrollX - .5, y - scrollY - .5, 0, side);
	}
}
