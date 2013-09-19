package ro7.game.screens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;

import cs195n.Vec2f;
import cs195n.Vec2i;
import ro7.engine.Application;
import ro7.engine.Screen;
import ro7.engine.sprites.FilledRectangle;
import ro7.engine.sprites.Menu;
import ro7.engine.sprites.Message;
import ro7.game.map.GameMap;
import ro7.game.map.MapParser;

public class TitleScreen extends Screen {

	private FilledRectangle background;
	private Message title;
	private Menu mapMenu;

	public TitleScreen(Application app) {
		super(app);
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDraw(Graphics2D g) {
		background.draw(g);
		title.draw(g);
		mapMenu.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 49 && keyCode <= 55) {
			MapParser parser = new MapParser();
			String mapFile = "maps/"
					+ mapMenu.getOption(keyCode - 49);
			GameMap map = parser.parseMap(new File(mapFile));
			app.pushScreen(new GameScreen(app, map));
		}
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResize(Vec2i newSize) {
		super.onResize(newSize);

		try {
			float titleX = windowSize.x / 2.5f;
			float titleY = windowSize.y / 5.0f;
			int fontSize = windowSize.x / 24;
			title = new Message("Tac!", fontSize*2, Color.WHITE,
					new Vec2f(titleX, titleY));

			float menuX = windowSize.x / 2.5f;
			float menuY = windowSize.y / 3.0f;
			Vec2f space = new Vec2f(windowSize.x / 20.0f, windowSize.y / 15.0f);
			mapMenu = new Menu(new Vec2f(menuX, menuY), space);

			mapMenu.addOption(new Message("small.map", fontSize / 2,
					Color.WHITE, new Vec2f(0.0f, 0.0f)));
			mapMenu.addOption(new Message("medium.map", fontSize / 2,
					Color.WHITE, new Vec2f(0.0f, 0.0f)));
			mapMenu.addOption(new Message("awesome.map", fontSize / 2,
					Color.WHITE, new Vec2f(0.0f, 0.0f)));
			mapMenu.addOption(new Message("empty.map", fontSize / 2,
					Color.WHITE, new Vec2f(0.0f, 0.0f)));
			mapMenu.addOption(new Message("bad.map", fontSize / 2, Color.WHITE,
					new Vec2f(0.0f, 0.0f)));
			mapMenu.addOption(new Message("badsize.map", fontSize / 2,
					Color.WHITE, new Vec2f(0.0f, 0.0f)));
			mapMenu.addOption(new Message("badsize2.map", fontSize / 2,
					Color.WHITE, new Vec2f(0.0f, 0.0f)));
			
			background = new FilledRectangle(new Vec2f(0.0f, 0.0f), new Vec2f(windowSize.x, windowSize.y), Color.BLACK, Color.BLACK);
		} catch (NullPointerException e) {
			System.out.println("No window size defined");
		}
	}

}
