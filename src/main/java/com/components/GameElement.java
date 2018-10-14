package com.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.dimension.Coordinate;
import com.dimension.Dimensions;
import com.infrastructure.Constants;
import com.infrastructure.Drawable;
import com.infrastructure.Element;
import com.infrastructure.GameElementShape;
import com.infrastructure.MoveType;

public class GameElement implements Element, Serializable {

	protected static Logger log = Logger.getLogger(GameElement.class);
	private String name;
	private Dimensions dimension;
	private Coordinate coordinate;
	private Coordinate startingPosition;
	private Color color;
	private BufferedImage image;
	private Drawable drawable;
	private boolean isVisible;
	public Coordinate getStartingPosition() {
		return startingPosition;
	}

	private int velX;
	private int velY;
	private MoveType moveType;
	private int initialvelX;
	private int initialvelY;
	private boolean inAir;
	private GameElementShape gameElementShape;
	private Dimensions actualDimension;
	private Coordinate actualCoordinate;
	private Rectangle2D rectBounds;
	private Ellipse2D ovalBounds;
	private String shapeType;

	public GameElement() {

	}

	public GameElement(Dimensions dimension, Coordinate coordinate, String name,
			int velX, int velY, String type) {
		this.dimension = dimension;
		this.coordinate = coordinate;
		this.velX = velX;
		this.velY = velY;
		this.shapeType = type;
		this.name = name;
		this.color = Color.black;
		this.ovalBounds = new Ellipse2D.Double();
		this.rectBounds = new Rectangle2D.Float();
		inAir = false;
		startingPosition = coordinate;
	}

	public GameElement(GameElement gameElement) {
		this.dimension = new Dimensions(gameElement.dimension.getWidth(),
				gameElement.dimension.getHeight());
		this.coordinate = new Coordinate(gameElement.coordinate.getX(),
				gameElement.coordinate.getY());
		this.velX = gameElement.velX;
		this.velY = gameElement.velY;
		this.initialvelX = gameElement.velX;
		this.initialvelY = gameElement.velY;
		this.shapeType = gameElement.shapeType;
		this.name = gameElement.name;
		this.color = gameElement.color;
		this.ovalBounds = new Ellipse2D.Double();
		this.rectBounds = new Rectangle2D.Float();
		this.drawable = gameElement.getDraw();
		this.isVisible = gameElement.isVisible();
		inAir = false;
		this.image = gameElement.image;
	}

	public static void setLog(Logger log) {
		GameElement.log = log;
	}

	public void setDimension(Dimensions dimension) {
		this.dimension = dimension;
	}

	public void setStartingPosition(Coordinate startingPosition) {
		this.startingPosition = startingPosition;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public void setInitialvelX(int initialvelX) {
		this.initialvelX = initialvelX;
	}

	public void setInitialvelY(int initialvelY) {
		this.initialvelY = initialvelY;
	}

	public void setActualDimension(Dimensions actualDimension) {
		this.actualDimension = actualDimension;
	}

	public void setOvalBounds(Ellipse2D ovalBounds) {
		this.ovalBounds = ovalBounds;
	}

	public int getInitialvelX() {
		return initialvelX;
	}

	public int getInitialvelY() {
		return initialvelY;
	}

//	public GameElement(Dimensions dimension, Coordinate coordinate, String name,
//			MoveType moveType, int velX, int velY, String type) {
//		this.dimension = dimension;
//		this.actualDimension = dimension;
//		this.coordinate = coordinate;
//		this.actualCoordinate = coordinate;
//		this.startingPosition = new Coordinate(coordinate.getX(),
//				coordinate.getY());
//		this.color = Color.BLACK;
//		this.moveType = moveType;
//		this.name = name;
//		this.initialvelX = velX;
//		this.initialvelY = velY;
//		this.velX = velX;
//		this.velY = velY;
//		this.ovalBounds = new Ellipse2D.Double();
//		this.rectBounds = new Rectangle2D.Float();
//		this.shapeType = type;
//		inAir = false;
//	}

	public void setActualDimension(Dimensions d, String type) {
		// used to set the actual dimensions, this functionality allows a
		// dynamic update of the object
		if (d.getWidth() > Constants.PREVIEW_RADIUS * 2.5
				&& d.getHeight() > Constants.PREVIEW_RADIUS * 2) {
			this.dimension = type.equals("Oval")
					? new Dimensions(Constants.PREVIEW_RADIUS)
					: new Dimensions(Constants.PREVIEW_RADIUS * 2,
							Constants.PREVIEW_RADIUS * 2);

		} else if (d.getWidth() > Constants.PREVIEW_RADIUS * 2.5
				&& type.equals("Rectangle")) {
			this.dimension = new Dimensions(
					(int) (Constants.PREVIEW_RADIUS * 2.5), d.getHeight());
		} else if (d.getHeight() > Constants.PREVIEW_RADIUS * 2
				&& type.equals("Rectangle")) {
			this.dimension = new Dimensions(d.getWidth(),
					Constants.PREVIEW_RADIUS * 2);
		} else {
			this.dimension = d;
		}
		this.actualDimension = d;

	}

	public void pushToBoard() {
		// This method replaces the preview values with the actual values
		// Also swaps the two, in case we want to show the object in preview
		// window again
//		System.out.print("Game Element - 73 - Pushing");
		Dimensions tempD = this.actualDimension;
		Coordinate tempC = this.actualCoordinate;
		this.actualDimension = this.dimension;
		this.actualCoordinate = this.coordinate;
		this.dimension = tempD;
		this.coordinate = tempC;
	}

	public void pushToPreview() {
		/*
		 * // This method replaces the preview values with the actual values //
		 * Also swaps the two, in case we want to show the object in gamePanel
		 * window again System.out.print("Game Element - 73 - Pushing");
		 * Dimensions tempD = this.dimension; Coordinate tempC =
		 * this.coordinate; this.dimension = this.actualDimension;
		 * this.coordinate = new
		 * Coordinate(Constants.PREVIEW_X_START,Constants.PREVIEW_Y_START);
		 * this.actualDimension = tempD; this.actualCoordinate = tempC; //
		 * this.actualCoordinate = this.coordinate;
		 */

	}

	public Rectangle2D getRectBounds() {
		return new Rectangle2D.Float(coordinate.getX(), coordinate.getY(),
				dimension.getWidth(), dimension.getHeight());
	}

	public String getShapeType() {
		return this.shapeType;
	}

	public void setShapeType(String type) {
		this.shapeType = type;
	}

	public void setActualCoordinate(Coordinate coordinate) {
		// TODO Auto-generated method stub
		this.actualCoordinate = coordinate;
	}

	public Coordinate getActualCoordinate() {
		// TODO Auto-generated method stub
		return this.actualCoordinate;
	}

	public Dimensions getActualDimensions() {
		// TODO Auto-generated method stub
		return this.actualDimension;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

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

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public int getX() {
		return coordinate.getX();
	}

	public void setX(int x) {
		this.coordinate.setX(x);
	}

	public int getY() {
		return coordinate.getY();
	}

	public void setY(int y) {
		this.coordinate.setY(y);
	}

	public int getWidth() {
		return dimension.getWidth();
	}

	public void setWidth(int width) {
		this.dimension.setWidth(width);
	}

	public int getHeight() {
		return dimension.getHeight();
	}

	public void setHeight(int height) {
		this.dimension.setHeight(height);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Dimensions getPosition() {
		return dimension;
	}

	public void setPosition(Dimensions position) {
		this.dimension = position;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		Image dimg = image.getScaledInstance(dimension.getWidth(), dimension.getHeight(),
		        Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(dimension.getWidth(), dimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(dimg, 0, 0, null);
		g2d.dispose();
		this.image = image;
	}

	public Drawable getDraw() {
		return drawable;
	}

	public void setDraw(Drawable draw) {
		this.drawable = draw;
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible) {
			drawable.draw(this, g);
		}
	}

	@Override
	public void reset() {
		coordinate = new Coordinate(startingPosition.getX(), startingPosition.getY());
		velX = initialvelX;
		velY = initialvelY;
	}

	@Override
	public void addComponent(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeComponent(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ObjectOutputStream op) {
		try {
			op.writeObject(this);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public Element load(ObjectInputStream ip) {
		try {
			GameElement obj = (GameElement) ip.readObject();
			return obj;
		} catch (ClassNotFoundException | IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GameElementShape getGameElementShape() {
		return gameElementShape;
	}

	public void setGameElementShape(GameElementShape gameElementShape) {
		this.gameElementShape = gameElementShape;
	}

	public boolean isInAir() {
		return inAir;
	}

	public void setInAir(boolean inAir) {
		this.inAir = inAir;
	}
}
