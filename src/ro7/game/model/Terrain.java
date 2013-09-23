package ro7.game.model;

import java.awt.Graphics2D;

import ro7.engine.sprites.SpriteSheet;
import ro7.game.sprites.TerrainSprite;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class Terrain {
	
	private boolean passable;
	private boolean projectiles;
	private float size;
	private Vec2f position;
	
	private TerrainSprite sprite;
	
	public Terrain(boolean passable, boolean projectiles, float size, SpriteSheet sheet, Vec2i sheetPosition) {
		this.passable = passable;
		this.projectiles = projectiles;
		this.size = size;
		this.position = new Vec2f(0.0f, 0.0f);
		
		this.sprite = new TerrainSprite(this.position, sheet, sheetPosition);
	}
	
	public Terrain(Terrain terrain) {
		this.passable = terrain.passable;
		this.projectiles = terrain.projectiles;
		this.size = terrain.size;
		this.position = terrain.position;
		
		this.sprite = new TerrainSprite(terrain.sprite);
	}
	
	public void setPosition(Vec2f newPosition) {
		this.position = newPosition;
		sprite.setPosition(newPosition);
	}
	
	public void draw(Graphics2D g) {
		sprite.draw(g);
	}

	public boolean isPassable() {
		return passable;
	}
	
	public Vec2i getMapPosition() {
		return new Vec2i((int)(position.x/size), (int)(position.y/size));
	}

	public Vec2f getPosition() {
		return position;
	}

}
