package com.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JComponent;

import com.components.Clock;
import com.components.GameElement;
import com.components.ScoreBoard;
import com.dimension.Coordinate;
import com.dimension.Dimensions;
import com.helper.Collider;
import com.infrastructure.ActionLink;
import com.infrastructure.ActionType;
import com.infrastructure.ButtonActionType;
import com.infrastructure.Constants;
import com.infrastructure.EventAction;
import com.infrastructure.EventType;
import com.infrastructure.MoveType;
import com.strategy.DrawOvalColor;
import com.strategy.DrawOvalImage;
import com.strategy.DrawRectangularColorShape;
import com.strategy.DrawRectangularImage;
import com.ui.CustomButton;
import com.ui.GUI;
import com.ui.SpriteProperties;

public class DesignController implements Serializable, MouseListener {

	private List<GameElement> graphicsElements;
	private GUI gui;
	private HashMap<String, ButtonActionType> controlElements;
	private List<Collider> colliders;
	private List<GameElement> scoreElementList;
	private List<String> gameEndElementsList;
	private List<String> gameScoreElementsList;
	private List<Collider> dynamicColliderList;

	private long timeConstraintinmilliSeconds;
	private boolean timerConstraintSpecified;
	private Clock clock;
	private MainController mainController;
	private ScoreBoard scoreBoard;
	private SpriteProperties spriteProperties;
	private int index;
	private int totalScore;

	private HashMap<EventType, ConcurrentLinkedQueue<ActionLink>> eventMap;

	public SpriteProperties getSpriteProperties() {
		return spriteProperties;
	}

	public List<Collider> getDynamicColliderList() {
		return dynamicColliderList;
	}

	private void setEventMap(SpriteProperties spriteProperties,
			GameElement gameElement) {
		
		for (EventAction eventActionObject : spriteProperties
				.getEventAction()) {
			EventType event = eventActionObject.getEvent();
			ActionType action = eventActionObject.getAction();
			int interval = eventActionObject.getInterval();

			if (!eventMap.containsKey(event)) {
				eventMap.put(event, new ConcurrentLinkedQueue<ActionLink>());
			}
			eventMap.get(event)
					.add(new ActionLink(gameElement, action, interval));
		}
	}

	public HashMap<EventType, ConcurrentLinkedQueue<ActionLink>> getEventMap() {
		return eventMap;
	}

	public void setSpriteProperties(SpriteProperties spriteProperties) {
		this.spriteProperties = spriteProperties;
		String gameElementShape = spriteProperties.getType();
		GameElement newGameElement;

		int quantity = spriteProperties.getQuantity();

		if (gameElementShape.equals("Circle")) {
			newGameElement = new GameElement(
					new Dimensions(spriteProperties.getRadius()),
					new Coordinate(Constants.PREVIEW_X_START,
							Constants.PREVIEW_Y_START),
					spriteProperties.getName(), spriteProperties.getVelX(),
					spriteProperties.getVelY(), gameElementShape);
			if (spriteProperties.getImage() != null) {
				newGameElement.setDraw(new DrawOvalImage());
				newGameElement.setImage(spriteProperties.getImage());
			} else {
				newGameElement.setDraw(new DrawOvalColor());
				newGameElement.setColor(spriteProperties.getColor());
			}
		} else {
			newGameElement = new GameElement(
					new Dimensions(spriteProperties.getWidth(),
							spriteProperties.getHeight()),
					new Coordinate(Constants.PREVIEW_X_START,
							Constants.PREVIEW_Y_START),
					spriteProperties.getName(), spriteProperties.getVelX(),
					spriteProperties.getVelY(), gameElementShape);
			if (spriteProperties.getImage() != null) {
				newGameElement.setDraw(new DrawRectangularImage());
				newGameElement.setImage(spriteProperties.getImage());
			} else {
				newGameElement.setDraw(new DrawRectangularColorShape());
				newGameElement.setColor(spriteProperties.getColor());
			}
		}
		if (spriteProperties.isGameEnd()) {
			gameEndElementsList.add(newGameElement.getName());
		}
		if (spriteProperties.isScoreElement()) {
			totalScore += quantity;
			this.gameScoreElementsList.add(newGameElement.getName());
		}
		newGameElement.setVisible(true);
		gui.getDesignPanel().getPreview().removeElements();
		gui.getDesignPanel().getPreview().addGameElement(newGameElement);
		index = graphicsElements.size();
		while (quantity-- > 0) {
			GameElement clonedElement = new GameElement(newGameElement);
			graphicsElements.add(clonedElement);
			setEventMap(spriteProperties, clonedElement);
		}
	}

	public DesignController(GUI gui) {
		this.gui = gui;
		controlElements = new HashMap<>();
		colliders = new ArrayList<>();
		scoreElementList = new ArrayList<>();
		setTimerConstraintSpecified(false);
		gameEndElementsList = new ArrayList<String>();
		gameScoreElementsList = new ArrayList<String>();
		eventMap = new HashMap<>();
		dynamicColliderList = new ArrayList<>();
		createBulletElement();
		scoreBoard = new ScoreBoard(new Coordinate(0, 0));
	};

	public void createBulletElement() {
		graphicsElements = new ArrayList<>();
		GameElement dummyBullet = new GameElement(new Dimensions(5, 20),
				new Coordinate(1000, 1000), "Bullet", 0, 10, "Rectangle");
		dummyBullet.setVisible(false);
		graphicsElements.add(dummyBullet);
	}

	public ButtonActionType getActionTypeBasedOnButtonCommand(String key) {
		if (controlElements.containsKey(key)) {
			return controlElements.get(key);
		}
		return null;
	}

	public void addTimer() {
		if (clock == null) {
			clock = new Clock(new Coordinate(Constants.TimerX, Constants.TimerY));
			gui.getControlPanel().addComponent(clock);
			gui.revalidate();
			gui.repaint();
		}
	}

	public void addScore() {
		scoreBoard = new ScoreBoard(new Coordinate(Constants.ScoreX, 600));
		gui.getControlPanel().addComponent(scoreBoard);
		gui.revalidate();
		gui.repaint();
	}

	public void removeGameElement(GameElement gameElement) {
		if (graphicsElements.contains(gameElement)) {
			graphicsElements.remove(gameElement);
		}
		if (gui.getGamePanel().getElements().contains(gameElement)) {
			gui.getGamePanel().getElements().remove(gameElement);
		}
	}

	public void addGameColliders(Collider collider) {
		colliders.add(collider);
	}

	public void addControlElement() {

		JComponent component = gui.getDesignPanel().getControlElement();
		if (component instanceof CustomButton) {
			CustomButton received = (CustomButton) component;
			CustomButton button = new CustomButton(received.getText(),
					received.getText(), received.getWidth(),
					received.getHeight(), mainController);
			button.setActionType(received.getActionType());
			controlElements.put(button.getActionCommand(),
					button.getActionType());
			gui.getControlPanel().add(button);
			gui.getControlPanel().addButtons(button);
		}
		gui.getControlPanel().revalidate();
	}

	public void pushToPreview(GameElement temp) {
		// TODO Auto-generated method stub
		temp.pushToPreview();
		this.gui.getDesignPanel().pushToPreview(temp);
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	public HashMap<String, ButtonActionType> getControlElements() {
		return controlElements;
	}

	public void setControlElements(
			HashMap<String, ButtonActionType> controlElements) {
		this.controlElements = controlElements;
	}

	public List<Collider> getColliders() {
		return colliders;
	}

	public void setColliders(List<Collider> colliders) {
		this.colliders = colliders;
	}

	public List<GameElement> getGraphicsElements() {
		return graphicsElements;
	}

	public void setGraphicsElements(List<GameElement> graphicsElements) {
		this.graphicsElements = graphicsElements;
	}

	public MainController getMainController() {
		return mainController;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public ScoreBoard getScoreBoard() {
		return scoreBoard;
	}

	public void setScoreBoard(ScoreBoard scoreBoard) {
		this.scoreBoard = scoreBoard;
	}

	public List<GameElement> getScoreElementList() {
		return scoreElementList;
	}

	public void setScoreElementList(List<GameElement> scoreElementList) {
		this.scoreElementList = scoreElementList;
	}

	public boolean isTimerConstraintSpecified() {
		return timerConstraintSpecified;
	}

	public void setTimerConstraintSpecified(boolean timerConstraintSpecified) {
		this.timerConstraintSpecified = timerConstraintSpecified;
	}

	public long getTimeConstraintinmilliSeconds() {
		return timeConstraintinmilliSeconds;
	}

	public void setTimeConstraintinmilliSeconds(
			long timeConstraintinmilliSeconds) {
		this.timeConstraintinmilliSeconds = timeConstraintinmilliSeconds;
	}

	public List<String> getGameEndElementsList() {
		return gameEndElementsList;
	}

	public List<String> getGameScoreElementsList() {
		return this.gameScoreElementsList;
	}

	public void setGameEndElementsList(List<String> gameEndElementsList) {
		this.gameEndElementsList = gameEndElementsList;
	}

	public void setGameScoreElementsList(List<String> gameScoreElementsList) {
		this.gameScoreElementsList = gameScoreElementsList;
	}

	public void setDynamicColliderList(List<Collider> dynamicColliderList) {
		this.dynamicColliderList = dynamicColliderList;
	}

	public void setEventMap(
			HashMap<EventType, ConcurrentLinkedQueue<ActionLink>> eventMap) {
		this.eventMap = eventMap;
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		if (index < graphicsElements.size()) {
			int x = mouseEvent.getX();
			int y = mouseEvent.getY();
			GameElement gameElement = graphicsElements.get(index++);
			gameElement.setX(x);
			gameElement.setY(y);
			gameElement.setStartingPosition(new Coordinate(x,y));
			gui.getGamePanel().addComponent(gameElement);
			gui.draw(null);
		}
	}
	public int getTotalScore() {
		return totalScore;
	}

	@Override
	public void mouseEntered(MouseEvent mouseEvent) {

	}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {

	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {

	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {

	}
}
