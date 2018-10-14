
package com.controller;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.commands.BounceCommand;
import com.commands.ChangeVelXCommand;
import com.commands.ChangeVelYCommand;
import com.commands.ExplodeCommand;
import com.commands.KeyJumpCommand;
import com.commands.KeyMoveCommand;
import com.commands.MoveCommand;
import com.commands.RandomMoveCommand;
import com.commands.TimerCommand;
import com.components.Clock;
import com.components.GameElement;
import com.components.ScoreBoard;
import com.helper.Collider;
import com.helper.CollisionChecker;
import com.infrastructure.ActionLink;
import com.infrastructure.ActionType;
import com.infrastructure.ButtonActionType;
import com.infrastructure.Command;
import com.infrastructure.Constants;
import com.infrastructure.Direction;
import com.infrastructure.Element;
import com.infrastructure.EventType;
import com.infrastructure.KeyType;
import com.infrastructure.Observer;
import com.strategy.DrawRectangularColorShape;
import com.timer.GameTimer;
import com.ui.CustomButton;
import com.ui.GUI;

public class MainController implements Observer, KeyListener, ActionListener {
	protected static Logger log = Logger.getLogger(MainController.class);
	private GUI gui;
	private GameTimer observable;
	private boolean isGamePaused;
	private Deque<Command> commandQueue;
	private DesignController designController;
	private CollisionChecker collisionChecker;
	private int numberOfTick = 0;
	private int numberOfKeyPress = 0;

	public MainController(GUI gui, GameTimer observable,
			DesignController designController,
			CollisionChecker collisionChecker) {
		this.gui = gui;
		this.observable = observable;

		this.designController = designController;
		this.collisionChecker = collisionChecker;
		isGamePaused = false;
		commandQueue = new ArrayDeque<Command>();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String commandText = event.getActionCommand();
		if (commandText.equals("AddControlElement")) {
			designController.addControlElement();
			gui.changeFocus();
		}
		if (commandText.equals("AddTimer")) {
			designController.addTimer();
			gui.changeFocus();
		}
		if (commandText.equals("AddScore")) {
			designController.addScore();
			gui.changeFocus();
		}
		ButtonActionType buttonActionType = designController
				.getActionTypeBasedOnButtonCommand(commandText);
		if (buttonActionType != null) {
			if (buttonActionType == ButtonActionType.PLAY) {
				start();
			} else if (buttonActionType == ButtonActionType.PAUSE) {
				pause();
			} else if (buttonActionType == ButtonActionType.REPLAY) {
				replay();
			} else if (buttonActionType == ButtonActionType.SAVE) {
				save();
			} else if (buttonActionType == ButtonActionType.LOAD) {
				load();
			} else if (buttonActionType == ButtonActionType.UNDO) {
				undo();
			} else if (buttonActionType == ButtonActionType.RESET) {
				gameReset();
			} else if (buttonActionType == ButtonActionType.BACKGROUND) {
				changeBackground();
			}
		}
	}

	private void changeBackground() {
		BufferedImage image = gui.openImageFileDialog();
		gui.getGamePanel().setImage(image);
		gui.draw(null);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.numberOfKeyPress += 1;
		if (designController.getEventMap().containsKey(EventType.KEY_PRESSED)) {
			KeyType key = null;
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				key = KeyType.LEFT;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				key = KeyType.RIGHT;
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				key = KeyType.UP;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				key = KeyType.DOWN;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				key = KeyType.W;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				key = KeyType.S;
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				key = KeyType.SPACE;
			}
			for (ActionLink actionLink : designController.getEventMap()
					.get(EventType.KEY_PRESSED)) {
				createActionCommand(actionLink, key, numberOfKeyPress);
			}
		}
	}

	public GameElement gameElementCreator(GameElement shooterElement) {

		for (GameElement gameElement : designController.getGraphicsElements()) {
			if (gameElement.getName().equals("Bullet")) {

				// Creating and Initializing a bullet at runtime
				GameElement clonedBullet = new GameElement(gameElement);
				int x = shooterElement.getX() + (shooterElement.getWidth() / 2)
						- (clonedBullet.getWidth() / 2);
				int y = shooterElement.getY() - clonedBullet.getHeight() - 1;
				clonedBullet.setX(x);
				clonedBullet.setY(y);
				clonedBullet.setVisible(true);
				clonedBullet.setDraw(new DrawRectangularColorShape());
				clonedBullet.setColor(Color.black);
				designController.getGraphicsElements().add(clonedBullet);

				// Adding Colliders for dynamic bullets
				List<Collider> newColliders = new ArrayList<>();
				for (Collider collider : designController
						.getDynamicColliderList()) {
					Collider newCollider = new Collider(collider);
					if (collider.getPrimaryElement().getName()
							.equals(clonedBullet.getName())) {
						newCollider.setPrimaryElement(clonedBullet);

					} else if (collider.getSecondaryElement().getName()
							.equals(clonedBullet.getName())) {
						newCollider.setSecondaryElement(clonedBullet);
					}
					newColliders.add(newCollider);
				}

				designController.getColliders().addAll(newColliders);

				gui.getGamePanel().addComponent(clonedBullet);
				return clonedBullet;
			}
		}
		return null;
	}

	public void createActionCommand(ActionLink actionLink, KeyType key,
			int count) {
		if (actionLink.getInterval() != 0
				&& count % actionLink.getInterval() != 0) {
			return;
		}
		switch (actionLink.getAction()) {
		case MOVE:
			if (key != null) {
				Command keyMoveCommand = new KeyMoveCommand(
						actionLink.getGameElement(), key);
				keyMoveCommand.execute();
				addCommand(keyMoveCommand);
			}
			Command moveCommand = new MoveCommand(actionLink.getGameElement());
			moveCommand.execute();
			addCommand(moveCommand);
			break;
		case JUMP:
			if (key == KeyType.W || key == KeyType.S) {
			Command jumpCommand = new KeyJumpCommand(
					actionLink.getGameElement(), key, designController.getColliders());
			System.out.println(designController.getColliders());
			jumpCommand.execute();
			addCommand(jumpCommand);
		}
			break;
		case SHOOTUP:
			if (key!=null && key!=KeyType.SPACE) {
				break;
			}
			GameElement shooterElement = actionLink.getGameElement();
			GameElement bullet = gameElementCreator(shooterElement);
			bullet.setVelY(-1 * bullet.getVelY());
			designController.getEventMap().get(EventType.ON_TIMER_TICK)
					.add(new ActionLink(bullet, ActionType.MOVE, 0));

			break;
		case SHOOTDOWN:
			if (key!=null && key!=KeyType.SPACE) {
				break;
			}
			GameElement shooterElement1 = actionLink.getGameElement();
			GameElement bullet1 = gameElementCreator(shooterElement1);
			bullet1.setY(bullet1.getY() + bullet1.getHeight()
					+ shooterElement1.getHeight() + 2);
			designController.getEventMap().get(EventType.ON_TIMER_TICK)
					.add(new ActionLink(bullet1, ActionType.MOVE, 0));
			break;
		case RANDOM:
			Command random = new RandomMoveCommand(actionLink.getGameElement());
			random.execute();
			addCommand(random);
			Command move = new MoveCommand(actionLink.getGameElement());
			move.execute();
			addCommand(move);
			break;
		case EXPLODE:
			Command explodeCommand = new ExplodeCommand(
					actionLink.getGameElement());
			explodeCommand.execute();
			addCommand(explodeCommand);
			break;
		case BOUNCE:
			Command bounceCommand = new BounceCommand(
					actionLink.getGameElement());
			bounceCommand.execute();
			addCommand(bounceCommand);
			break;
		default:
			break;
		}
	}

	@Override
	public void update() {
		this.numberOfTick += 1;
		if (designController.getClock() != null) {
			TimerCommand timerCommand = new TimerCommand(
					designController.getClock());
			timerCommand.execute();
			addCommand(timerCommand);
		}
		
		for (Collider collider : designController.getColliders()) {
			collider.execute(this);
		}

		List<GameElement> graphicsElements = designController
				.getGraphicsElements();
		for (GameElement element : graphicsElements) {
			Direction direction = collisionChecker
					.checkCollisionBetweenGameElementAndBounds(element);
			if (direction == direction.X) {
				Command command = new ChangeVelXCommand(element);
				command.execute();
				addCommand(command);
			} else if (direction == direction.Y) {
				Command command = new ChangeVelYCommand(element);
				command.execute();
				addCommand(command);
			}
		}
 
		if (designController.getEventMap()
				.containsKey(EventType.ON_TIMER_TICK)) {
			for (ActionLink actionLink : designController.getEventMap()
					.get(EventType.ON_TIMER_TICK)) {
				createActionCommand(actionLink, null, numberOfTick);
			}
		}
		designController.getScoreBoard().setScore(designController.getTotalScore() - getVisibleScoreElementCount());

		gui.draw(null);
		if (checkGameEnd()) {
			gameOver();
		}

	}

	public boolean checkGameEnd() {
		boolean present = false;
		for (String s : designController.getGameEndElementsList()) {
			if (getVisibleGameEndCount(s) == 0) {
				return true;
			}
		}
		return false;
	}

	public int getVisibleGameEndCount(String name) {
		int count = 0;
		for (GameElement g : designController.getGraphicsElements()) {
			if (g.getName().equals(name) && g.isVisible()) {
				count += 1;
			}
		}
		return count;
	}
	public int getVisibleScoreElementCount() {
		int count = 0;
		for (String s: designController.getGameScoreElementsList()) {
			for (GameElement g : designController.getGraphicsElements()) {
				if (g.getName().equals(s) && g.isVisible()) {
					count += 1;
				}
			}
		}
		return count;
	}

	public void start() {
		if (isGamePaused) {
			unPause();
		}
		gui.changeFocus();
		observable.registerObserver(this);
	}

	public void undo() {
		if (!isGamePaused) {
			pause();
			undoAction();
			unPause();
		} else {
			undoAction();
		}
		gui.changeFocus();
	}

	private void undoAction() {
		int count = 0;
		while (count != Constants.TIMER_COUNT) {
			Command val = commandQueue.pollLast();
			if (val == null)
				break;
			if (val instanceof TimerCommand) {
				count++;
			}
			val.undo();
		}
		gui.revalidate();
		gui.draw(null);
	}

	private void replayAction() {
		// TODO Auto-generated method stub
		final Iterator<Command> itr = commandQueue.iterator();
		new Thread() {
			public void run() {
				while (itr.hasNext()) {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							Command val = (Command) itr.next();

							@Override
							public void run() {
								// TODO Auto-generated method stub
								val.execute();
								gui.draw(null);
								try {
									currentThread();
									Thread.sleep(10);
								} catch (InterruptedException e) {
									log.error(e.getMessage());
								}
							}
						});
					} catch (InvocationTargetException
							| InterruptedException e) {
						log.error(e.getMessage());
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				}
			}
		}.start();
	}

	public void replay() {
		pause();
		gameReset();
		replayAction();
		gui.changeFocus();
	}

	public void gameReset() {
		for (GameElement gameElement : designController.getGraphicsElements()) {
			gameElement.reset();
		}
		Clock clock = designController.getClock();
		clock.reset();
	}

	public void pause() {
		isGamePaused = true;
		if (!observable.isObserverListEmpty()) {
			observable.removeObserver(this);
		}
	}

	public void unPause() {
		isGamePaused = false;
		observable.registerObserver(this);
	}

	public void save() {
		pause();
		try {
			String fileName = gui.showSaveDialog();
			if (!fileName.isEmpty()) {
				FileOutputStream fileOut = new FileOutputStream(fileName);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				List<Element> list = gui.getGamePanel().getElements();
				out.writeObject(list);
				list = gui.getControlPanel().getElements();
				for (Element ele : list) {
					if (ele instanceof Clock) {
						designController.setClock((Clock) ele);
						break;
					}
				}
				out.writeObject(list);
				List<CustomButton> buttons = gui.getControlPanel().getButtons();
				out.writeObject(buttons);
				out.writeObject(commandQueue);
				out.writeObject(designController.getColliders());
				out.writeObject(designController.getControlElements());
				
				out.writeObject(designController.getGraphicsElements());
				out.writeObject(designController.getEventMap());
				out.writeObject(designController.getGameEndElementsList());
				out.writeObject(designController.getGameScoreElementsList());
				out.writeObject(designController.getDynamicColliderList());
				out.writeObject(designController.getClock());
				out.writeObject(designController.getScoreBoard());
				
				out.close();
				fileOut.close();
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void load() {
		pause();
		commandQueue.clear();
		try {
			String fileName = gui.showOpenDialog();
			if (!fileName.isEmpty()) {
				FileInputStream fileIn = new FileInputStream(fileName);
				ObjectInputStream in = new ObjectInputStream(fileIn);

				ArrayList<Element> gameElements = (ArrayList<Element>) in
						.readObject();
				ArrayList<Element> controlpanelElements = (ArrayList<Element>) in
						.readObject();
				ArrayList<CustomButton> controlpanelButtons = (ArrayList<CustomButton>) in
						.readObject();

				gui.getGamePanel().setElement(gameElements);
				gui.getControlPanel().reset();
				gui.getControlPanel().setElements(controlpanelElements);
				gui.getControlPanel().setButtons(new ArrayList<CustomButton>());
				for (CustomButton button : controlpanelButtons) {
					button.addController(this);
					gui.getControlPanel().addButtons(button);
				}

				List<GameElement> elements = new ArrayList<>();
				for (Element e : gameElements) {
					elements.add((GameElement) e);
				}
				designController.setGraphicsElements(elements);

				commandQueue.clear();
				Deque<Command> loadCmdQueue = (Deque<Command>) in.readObject();
				commandQueue.addAll(loadCmdQueue);

				List<Collider> colliders = (List<Collider>) in.readObject();
				HashMap<String, ButtonActionType> controlElements = (HashMap<String, ButtonActionType>) in
						.readObject();

				designController.setColliders(colliders);
				designController.setControlElements(controlElements);

				List<GameElement> graphicsElements = (List<GameElement>) in.readObject();
				designController.setGraphicsElements(graphicsElements);
				
				HashMap<EventType, ConcurrentLinkedQueue<ActionLink>> eventMap = (HashMap<EventType, ConcurrentLinkedQueue<ActionLink>>) in.readObject();
				designController.setEventMap(eventMap);
				
				List<String> gameEndElementsList = (List<String>) in.readObject();
				designController.setGameEndElementsList(gameEndElementsList);
				
				List<String> gameScoreElementsList = (List<String>) in.readObject();
				designController.setGameScoreElementsList(gameScoreElementsList);
				
				List<Collider> dynamicColliderList = (List<Collider>) in.readObject();
				designController.setDynamicColliderList(dynamicColliderList);
				
				Clock clock = (Clock) in.readObject();
				designController.setClock(clock);

				ScoreBoard scoreBoard = (ScoreBoard) in.readObject();
				designController.setScoreBoard(scoreBoard);
				
				in.close();
				fileIn.close();
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
		gui.revalidate();
		gui.draw(null);
		gui.changeFocus();

	}

	public void gameOver() {
		pause();
		Object[] options = { "Exit" };
		String outputMsg = new String();
		outputMsg = "Your Score is "
				+ designController.getScoreBoard().getScore();
		int a = JOptionPane.showOptionDialog(gui.getGamePanel(), outputMsg,
				"Game Over", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, null);

		if (a == JOptionPane.OK_OPTION) {
			for(Frame frame : GUI.getFrames()) {
				frame.dispose();
			}
		}
	}

	public Command createCommand(GameElement element) {
		return new MoveCommand(element);
	}

	public void addCommand(Command command) {
		commandQueue.add(command);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
