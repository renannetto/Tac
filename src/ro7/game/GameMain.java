package ro7.game;

import ro7.engine.Application;
import ro7.game.screens.TitleScreen;
import cs195n.Vec2i;

public class GameMain {

	public static void main(String[] par) {
		Application app = new Application("Tac", false, new Vec2i(960, 864));
		app.pushScreen(new TitleScreen(app));
		app.startup();
	}

}
