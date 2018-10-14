package com.helper;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.commands.ChangeVelXCommand;
import com.commands.ChangeVelYCommand;
import com.commands.ExplodeCommand;
import com.commands.MoveCommand;
import com.commands.NullCommand;
import com.commands.RandomMoveCommand;
import com.commands.ResetCommand;
import com.commands.StickCommand;
import com.components.GameElement;
import com.controller.MainController;
import com.infrastructure.ActionType;
//import com.infrastructure.CollisionType;
import com.infrastructure.Command;
import com.infrastructure.Direction;

@SuppressWarnings("serial")
public class Collider implements Serializable {

	public static final Logger logger = Logger.getLogger(Collider.class);
	private GameElement primaryElement;
	private GameElement secondaryElement;
	private ActionType primaryCollisionAction;
	private ActionType secondaryCollisionAction;
	private CollisionChecker collisionChecker;
	private ArrayList<Command> eventList;

	public Collider(GameElement element1, GameElement element2,
			ActionType collisionType1, ActionType collisionType2,
			CollisionChecker collisionChecker, ArrayList<Command> eventList) {
		this.primaryElement = element1;
		this.secondaryElement = element2;
		this.primaryCollisionAction = collisionType1;
		this.secondaryCollisionAction = collisionType2;
		this.collisionChecker = collisionChecker;
		this.eventList = eventList;
	}


	public Collider(Collider newCollider) {
		this.primaryElement = newCollider.primaryElement;
		this.secondaryElement = newCollider.secondaryElement;
		this.primaryCollisionAction = newCollider.primaryCollisionAction;
		this.secondaryCollisionAction = newCollider.secondaryCollisionAction;
		this.collisionChecker = newCollider.collisionChecker;
		this.eventList = newCollider.eventList;
	}

	public void execute(MainController controller) {
		
		if (!primaryElement.isInAir() && !secondaryElement.isInAir() && primaryElement.isVisible() && secondaryElement.isVisible()
				&& collisionChecker.checkIntersectionBetweenElements(
						primaryElement, secondaryElement)) {
			if (eventList != null) {
				for (Command eventCommand : eventList) {
					eventCommand.execute();
					controller.addCommand(eventCommand);
				}
			}
			Command command = getCollisionAction(primaryElement,
					primaryCollisionAction);

			if (primaryCollisionAction == ActionType.BOUNCE) {
				Direction direction = collisionChecker
						.checkCollisionBetweenGameElements(primaryElement,
								secondaryElement);
				changeDirectionsOnCollision(primaryElement, direction,
						controller);
			} else if (primaryCollisionAction == ActionType.STICK) {
				command = new StickCommand(primaryElement, secondaryElement.getVelX(), secondaryElement.getVelY());
			}
			
			command.execute();
			controller.addCommand(command);
			command = getCollisionAction(secondaryElement,
					secondaryCollisionAction);
			
			if (secondaryCollisionAction == ActionType.BOUNCE) {
				Direction direction = collisionChecker
						.checkCollisionBetweenGameElements(secondaryElement,
								primaryElement);
				changeDirectionsOnCollision(secondaryElement, direction,
						controller);
			} else if (secondaryCollisionAction == ActionType.STICK) {
				command = new StickCommand(secondaryElement, primaryElement.getVelX(), primaryElement.getVelY());
			}
//			System.out.println("Collision between " + primaryElement.getName() + " and " + secondaryElement.getName());
			command.execute();
			controller.addCommand(command);
		}
	}

	public Command getCollisionAction(GameElement gameElement,
			ActionType collisionType) {
		switch (collisionType) {
		case EXPLODE:
			return new ExplodeCommand(gameElement);
		case DO_NOTHING:
			return new NullCommand(gameElement);
		case BOUNCE:
			return new MoveCommand(gameElement);
		case RESET:
			return new ResetCommand(gameElement);
		case RANDOM:
			return new RandomMoveCommand(gameElement);
		default:
			return new NullCommand(gameElement);
		}
	}

	public void changeDirectionsOnCollision(GameElement element,
			Direction direction, MainController controller) {
		Command changeVelXCommand = null;
		Command changeVelYCommand = null;
		if (direction == Direction.X) {
			changeVelXCommand = new ChangeVelXCommand(element);
		}
		if (direction == Direction.Y) {
			changeVelYCommand = new ChangeVelYCommand(element);
		}
		if (direction == Direction.BOTH) {
			changeVelXCommand = new ChangeVelXCommand(element);
			changeVelYCommand = new ChangeVelYCommand(element);
		}
		if (changeVelXCommand != null) {
			changeVelXCommand.execute();
			controller.addCommand(changeVelXCommand);
		}
		if (changeVelYCommand != null) {
			changeVelYCommand.execute();
			controller.addCommand(changeVelYCommand);
		}
	}

	public GameElement getPrimaryElement() {
		return primaryElement;
	}

	public void setPrimaryElement(GameElement primaryElement) {
		this.primaryElement = primaryElement;
	}

	public GameElement getSecondaryElement() {
		return secondaryElement;
	}

	public void setSecondaryElement(GameElement secondaryElement) {
		this.secondaryElement = secondaryElement;
	}

	public ActionType getPrimaryCollisionAction() {
		return primaryCollisionAction;
	}

	public void setPrimaryCollisionAction(ActionType primaryCollisionAction) {
		this.primaryCollisionAction = primaryCollisionAction;
	}

	public ActionType getSecondaryCollisionAction() {
		return secondaryCollisionAction;
	}

	public void setSecondaryCollisionAction(
			ActionType secondaryCollisionAction) {
		this.secondaryCollisionAction = secondaryCollisionAction;
	}
}
