package ro7.engine.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import cs195n.Vec2f;

/**
 * @author ro7
 * Sprite that represents a text message
 */
public class Message extends Sprite {
	
	private String text;
	private int fontSize;
	private Color fontColor;
	
	public Message(String text, int fontSize, Color fontColor, Vec2f position) {
		super(position);
		this.text = text;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
	}

	public Message(String text, Message message) {
		super(message.position);
		this.text = text;
		this.fontSize = message.fontSize;
		this.fontColor = message.fontColor;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setFont(new Font("Arial", Font.PLAIN, fontSize));
		g.setColor(fontColor);
		g.drawString(text, position.x, position.y);
	}

	public String getText() {
		return text;
	}

}
