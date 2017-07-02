package manuk.path.game.painter;

import android.graphics.Color;
import manuk.path.game.controller.Controller;

public class PaintElement {
	static final int FRAME_COLOR = Color.GRAY, FRAME_SIZE = 5;
	
	double left, top, width, height;
	int color;
	
	public PaintElement(double left, double top, double width, double height, int color) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public void handleInput(Controller controller) {
	}
	
	public void draw(Painter painter) {
		painter.drawRect(left, top, width, height, color);
		painter.drawRect(left, top, width, height, FRAME_COLOR, FRAME_SIZE);
	}
}
