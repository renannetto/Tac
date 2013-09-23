package ro7.engine.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

import cs195n.Vec2f;

/**
 * @author ro7
 * Sprite that represents a colored circle
 */
public class Circle extends Sprite {
	
	private float size;
	private Color borderColor;
	private Color fillColor;

	public Circle(Vec2f position, float size, Color borderColor, Color fillColor) {
		super(position);
		this.size = size;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
	}

	@Override
	public void draw(Graphics2D g) {		
		Ellipse2D circle = new Ellipse2D.Float(position.x, position.y, size, size);
		
		Stroke oldStroke = g.getStroke();
		g.setColor(borderColor);
		g.setStroke(new BasicStroke(5.0f));
		g.draw(circle);
		g.setStroke(oldStroke);
		
		g.setColor(fillColor);
		g.fill(circle);
	}

}
