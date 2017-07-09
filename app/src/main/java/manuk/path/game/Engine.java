package manuk.path.game;

import manuk.path.MySurface;
import manuk.path.game.controller.Controller;
import manuk.path.game.painter.MapPainter;
import manuk.path.game.painter.Painter;
import manuk.path.game.render.MyRenderer;
import manuk.path.game.render.texture.CharGlyphTextureGroup;
import manuk.path.game.util.Frames;
import manuk.path.game.util.Measurements;

import static manuk.path.game.util.Measurements.*;

public class Engine implements Runnable {
	private Frames frames;
	private Painter painter;
	private Controller controller;
	private World world;
	
	public Engine() {
		world = new World(MAP_WIDTH, MAP_LENGTH, MAP_HEIGHT);
		controller = new Controller();
	}
	
	public void setupRenderer(MySurface mySurface, MyRenderer myRenderer, int screenWidth, int screenHeight) {
		Measurements.init(screenWidth, screenHeight);
		CharGlyphTextureGroup.init();
		painter = new Painter(mySurface, myRenderer);
		MapPainter.setPainter(painter);
		mySurface.setController(controller);
	}
	
	private void update() {
		world.update(controller);
		controller.refreshTouchStates();
	}
	
	private void draw() {
		painter.prep();
		world.draw(painter);
		frames.draw(painter);
		painter.post();
	}
	
	private void sleep(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		frames = new Frames();
		while (painter == null)
			sleep(500);
		while (frames.running) {
			if (!world.gameOver)
				update();
			draw();
			sleep(5);
		}
	}
	
	public void stop() {
		frames.running = false;
	}
}

// todo: fix side darwing of character
// todo: lighting and shadow
// todo: textures
// todo: map entities and collision
// todo: basic combat
// todo: models
// todo: better camera perspective
// todo: enemy path finding

// todo: level up - 10 life, 10 mana
// todo: stats: choose 4 of (life, life regen, mana, mana regen, evs, armour, shield, resists, attack/cast speed, attack/cast dmg, crit chance/dmg, status duration, aoe) 
// todo: item types
// todo: item enchanting and crafting costs
// todo: damage types
// todo: skill system
// todo: calculate VIEW_STRETCH_Z correctly
// todo: shrines