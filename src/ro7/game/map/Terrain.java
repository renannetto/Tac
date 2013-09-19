package ro7.game.map;

import java.awt.Color;
import java.awt.Graphics2D;

import ro7.engine.sprites.FilledRectangle;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class Terrain {
	
	private boolean passable;
	private boolean projectiles;
	private float size;
	private Vec2f position;
	
	public Terrain(boolean passable, boolean projectiles, float size) {
		this.passable = passable;
		this.projectiles = projectiles;
		this.size = size;
		this.position = new Vec2f(0.0f, 0.0f);
	}
	
	public Terrain(Terrain terrain) {
		this.passable = terrain.passable;
		this.projectiles = terrain.projectiles;
		this.size = terrain.size;
		this.position = terrain.position;
	}
	
	public void setPosition(Vec2f newPosition) {
		this.position = newPosition;
	}
	
	public void draw(Graphics2D g) {
		Color borderColor = Color.WHITE;
		Color fillColor;
		if (passable) {
			fillColor = Color.GRAY;
		} else {
			fillColor = Color.BLACK;
		}
		
		FilledRectangle terrain = new FilledRectangle(position, new Vec2f(size, size), borderColor, fillColor);
		terrain.draw(g);
	}

	public boolean isPassable() {
		return passable;
	}
	
	public Vec2i getMapPosition() {
		return new Vec2i((int)(position.y/size), (int)(position.x/size));
	}

	public Vec2f getPosition() {
		return position;
	}

	public boolean inside(Vec2f min, Vec2f max) {
		return (position.x > min.x && position.y > min.y && position.x < max.x && position.y < max.y);
	}

}
