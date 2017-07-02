package manuk.path.game.painter;

import manuk.path.game.controller.Controller;
import manuk.path.game.controller.Controller.Touch;

public class ClickablePaintElement extends PaintElement {
	int pressedColor;
	public boolean isPressed;
	
	public ClickablePaintElement(double left, double top, double width, double height, int color, int pressedColor) {
		super(left, top, width, height, color);
		this.pressedColor = pressedColor;
	}
	
	public void handleInput(Controller controller) {
		isPressed = false;
		for (Touch touch : controller.touch)
			if (touch.isDown() && touch.x > left && touch.x < left + width && touch.y > top && touch.y < top + height) {
				isPressed = true;
				touch.state = Touch.STATE_CONSUMED;
				break;
			}
	}
	
	public void draw(Painter painter) {
		painter.drawRect(left, top, width, height, isPressed ? pressedColor : color);
		painter.drawRect(left, top, width, height, FRAME_COLOR, FRAME_SIZE);
	}
}
