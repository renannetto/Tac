package ro7.engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import cs195n.SwingFrontEnd;
import cs195n.Vec2i;

public class Application extends SwingFrontEnd {

	private List<Screen> screens;
	private Screen topScreen;
	private Vec2i windowSize;

	public Application(String title, boolean fullscreen) {
		super(title, fullscreen);
		screens = new ArrayList<Screen>();
	}

	public Application(String title, boolean fullscreen, Vec2i windowSize) {
		super(title, fullscreen, windowSize);
		screens = new ArrayList<Screen>();
	}

	public Application(String title, boolean fullscreen, Vec2i windowSize,
			int closeOp) {
		super(title, fullscreen, windowSize, closeOp);
		screens = new ArrayList<Screen>();
	}

	/**
	 * Add a new screen to the application
	 * @param screen screen to be added
	 */
	public void pushScreen(Screen screen) {
		screens.add(0, screen);
		topScreen = screen;
		topScreen.onResize(windowSize);
	}

	/**
	 * Remove the current screen from the application
	 */
	public void popScreen() {
		if (!screens.isEmpty()) {
			screens.remove(0);
			if (!screens.isEmpty()) {
				topScreen = screens.get(0);
				topScreen.onResize(windowSize);
			} else  {
				topScreen = null;
			}
		}
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		try {
			topScreen.onTick(nanosSincePreviousTick);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onDraw(Graphics2D g) {
		try {
			topScreen.onDraw(g);
		} catch (NullPointerException exception) {
			//System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		try {
			topScreen.onKeyTyped(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		try {
			topScreen.onKeyPressed(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		try {
			topScreen.onKeyReleased(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		try {
			topScreen.onMouseClicked(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		try {
			topScreen.onMousePressed(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		try {
			topScreen.onMouseReleased(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		try {
			topScreen.onMouseDragged(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		try {
			topScreen.onMouseMoved(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		try {
			topScreen.onMouseWheelMoved(e);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}

	@Override
	protected void onResize(Vec2i newSize) {
		try {
			windowSize = newSize;
			topScreen.onResize(newSize);
		} catch (NullPointerException exception) {
			System.out.println("There is no current Screen");
		}
	}
	
	public Vec2i getWindowSize() {
		return windowSize;
	}

}
