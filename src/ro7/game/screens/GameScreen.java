package ro7.game.screens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ro7.engine.Application;
import ro7.engine.Screen;
import ro7.engine.Viewport;
import ro7.engine.sprites.Message;
import ro7.game.map.GameMap;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class GameScreen extends Screen {

	private final float MOVE_FACTOR = 10.0f;
	private final float ZOOM_FACTOR = 1.1f;

	private GameMap map;
	private Viewport viewport;

	private String warningMessage;
	private Message warning;
	private Message win;
	private Message lose;

	public GameScreen(Application app, GameMap map) {
		super(app);
		this.map = map;
		if (map == null) {
			warningMessage = "Invalid map! Go back to the title screen";
		}
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		try {
			if (map != null) {
				//map.updateComputer(nanosSincePreviousTick);
				map.moveUnits();
				map.checkAliveUnits();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void onDraw(Graphics2D g) {
		if (map != null) {
			System.out.println("Transform before viewport: " + g.getTransform());
			viewport.draw(g);
			System.out.println("Transform after viewport: " + g.getTransform());
			System.out.println("----------------------------------------");
			if (map.win()) {
				win.draw(g);
			} else if (map.lose()) {
				lose.draw(g);
			}
		} else {
			warning.draw(g);
		}
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case 27:
			app.popScreen();
		case 37:
			viewport.translate(new Vec2f(-MOVE_FACTOR, 0.0f));
			break;
		case 38:
			viewport.translate(new Vec2f(0.0f, -MOVE_FACTOR));
			break;
		case 39:
			viewport.translate(new Vec2f(MOVE_FACTOR, 0.0f));
			break;
		case 40:
			viewport.translate(new Vec2f(0.0f, MOVE_FACTOR));
			break;
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
		Point point = e.getPoint();

		Vec2f gamePosition = viewport.screenToGame(new Vec2f(point.x, point.y));

		map.clicked(gamePosition);
	}

	@Override
	public void onMouseReleased(MouseEvent e) {

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
		int rotation = e.getWheelRotation();
		if (rotation < 0) {
			viewport.zoomIn(-rotation * ZOOM_FACTOR);
		} else {
			viewport.zoomOut(rotation * ZOOM_FACTOR);
		}
	}

	@Override
	public void onResize(Vec2i newSize) {
		super.onResize(newSize);

		try {
			if (viewport != null) {
				Vec2f gamePosition = viewport.getGamePosition();
				Vec2f scale = viewport.getScale();
				viewport = new Viewport(new Vec2f(0.0f, 0.0f), new Vec2f(
						windowSize.x, windowSize.y), map, scale, gamePosition);
			} else {
				viewport = new Viewport(new Vec2f(0.0f, 0.0f), new Vec2f(
						windowSize.x, windowSize.y), map,
						new Vec2f(1.0f, 1.0f), new Vec2f(0.0f, 0.0f));
			}

			float titleX = windowSize.x / 8.0f;
			float titleY = windowSize.y / 5.0f;
			Vec2f titlePosition = new Vec2f(titleX, titleY);
			int fontSize = windowSize.x / 24;
			warning = new Message(warningMessage, fontSize, Color.BLACK,
					titlePosition);
			
			System.out.println("Window size: " + windowSize);
			float endX = windowSize.x / 2.5f;
			float endY = windowSize.y / 2.0f;
			Vec2f endPosition = new Vec2f(endX, endY);
			win = new Message("You won!", fontSize, Color.BLACK, endPosition);
			lose = new Message("You lost!", fontSize, Color.BLACK, endPosition);
		} catch (NullPointerException e) {
			System.out.println("No window size defined");
		}
	}

}
