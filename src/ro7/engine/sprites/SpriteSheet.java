package ro7.engine.sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cs195n.Vec2f;
import cs195n.Vec2i;

public class SpriteSheet {
	
	private BufferedImage sheet;
	private Vec2i frameDimensions;
	private Vec2i padding;
	
	public SpriteSheet(String sheetFile, Vec2i dimensions, Vec2i padding) {
		try {
			sheet = ImageIO.read(new File(sheetFile));
			this.frameDimensions = dimensions;
			this.padding = padding;
		} catch (IOException e) {
			System.out.println("Invalid sprite sheet file");
		}
	}
	
	/**
	 * Draw sprite correspondent to the given position on the sprite 
	 * sheet
	 * @param g Graphics object used to draw
	 * @param sheetPosition position of the sprite on the sprite sheet
	 * @param position position on the screen to draw
	 */
	public void draw(Graphics2D g, Vec2i sheetPosition, Vec2f position) {
		int x = (sheetPosition.x*frameDimensions.x) + ((sheetPosition.x+1)*padding.x);
		int y = (sheetPosition.y*frameDimensions.y) + ((sheetPosition.y+1)*padding.y);
		BufferedImage subImage = sheet.getSubimage(x, y, frameDimensions.x, frameDimensions.y);
		g.drawImage(subImage, (int)position.x, (int)position.y, null);
	}

}
