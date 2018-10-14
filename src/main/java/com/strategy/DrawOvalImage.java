package com.strategy;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.components.GameElement;
import com.dimension.Coordinate;
import com.dimension.Dimensions;
import com.infrastructure.Drawable;

public class DrawOvalImage implements Drawable,Serializable{
	
	@Override
	public void draw(GameElement element, Graphics g) {
		Dimensions dimension = element.getPosition();
		Coordinate coordinate = element.getCoordinate();
	
		BufferedImage resized = new BufferedImage(dimension.getWidth(), dimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		Image dimg = element.getImage().getScaledInstance(dimension.getWidth(), dimension.getHeight(),
		        Image.SCALE_SMOOTH);
		g2d.setClip(new Ellipse2D.Float(coordinate.getX(), coordinate.getY(), dimension.getWidth(), dimension.getWidth()));
		g2d.drawImage(dimg, 0, 0, null);
		g2d.dispose();

		g.drawImage(dimg, coordinate.getX(), coordinate.getY(),dimension.getWidth(), dimension.getHeight(), null);
	}
}