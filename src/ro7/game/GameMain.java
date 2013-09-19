package ro7.game;

import cs195n.Vec2i;
import ro7.engine.Application;
import ro7.game.screens.TitleScreen;

public class GameMain {

	public static void main(String[] par) {
		Application app = new Application("Tac", false, new Vec2i(1000, 900));
		app.pushScreen(new TitleScreen(app));
		app.startup();
	}

}
