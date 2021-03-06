package manuk.path.game.userinterface;

import android.graphics.Color;
import manuk.path.game.userinterface.element.ClickablePaintElement;
import manuk.path.game.userinterface.element.Joystick;
import manuk.path.game.userinterface.element.PaintBar;

public class PlayUserInterface extends UserInterface {
	public Joystick joystick;
	public PaintBar lifeBar, staminaBar, expBar;
	public ClickablePaintElement dashButton, sprintButton;
	public ClickablePaintElement pauseButton;
	
	public PlayUserInterface() {
		elements.addHead(joystick = new Joystick(1. / 12, 9. / 14, 1. / 6, 2. / 7, Color.GREEN, Color.RED));
		elements.addHead(lifeBar = new PaintBar(.05, .01, .43, .02, Color.RED));
		elements.addHead(staminaBar = new PaintBar(.52, .01, .43, .02, Color.BLUE));
		elements.addHead(expBar = new PaintBar(.05, .04, .90, .01, Color.GREEN));
		elements.addHead(dashButton = new ClickablePaintElement(.65, .8, .09, .15, Color.WHITE, Color.GRAY));
		elements.addHead(sprintButton = new ClickablePaintElement(.8, .8, .09, .15, Color.WHITE, Color.GRAY));
		elements.addHead(pauseButton = new ClickablePaintElement(.88, 0, .12, .2, Color.WHITE, Color.GRAY));
	}
}
