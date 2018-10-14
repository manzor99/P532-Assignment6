package com.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import com.infrastructure.EventAction;

public class SpriteProperties {
	private int velX;
	private int velY;
	private String name;
	private BufferedImage image;
	private Color color;
	private int width;
	private int height;
	private String type;
	private boolean scoreElement;
	private boolean gameEnd;

	public boolean isScoreElement() {
		return scoreElement;
	}

	public boolean isGameEnd() {
		return gameEnd;
	}

	public SpriteProperties(int velX, int velY, String name,
			BufferedImage image, Color color, int width, int height,
			String type, int radius, int quantity,
			List<EventAction> eventAction, boolean scoreElement,
			boolean gameEnd) {
		super();
		this.velX = velX;
		this.velY = velY;
		this.name = name;
		this.image = image;
		this.color = color;
		this.width = width;
		this.height = height;
		this.type = type;
		this.radius = radius;
		this.quantity = quantity;
		this.eventAction = eventAction;
		this.scoreElement = scoreElement;
		this.gameEnd = gameEnd;
	}

	private int radius;
	private int quantity;
	private List<EventAction> eventAction;

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public List<EventAction> getEventAction() {
		return eventAction;
	}

	public void setEventAction(List<EventAction> eventAction) {
		this.eventAction = eventAction;
	}

}
