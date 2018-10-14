package com.commands;

import org.apache.log4j.Logger;

import com.components.GameElement;
import com.infrastructure.Command;
import com.infrastructure.Constants;
import com.infrastructure.KeyType;

@SuppressWarnings("serial")
public class KeyMoveCommand implements Command {

	public static final Logger logger = Logger.getLogger(KeyMoveCommand.class);
	private GameElement gameElement;
	private KeyType keyType;
	private int prevX;
	private int prevY;
	
	public KeyMoveCommand(GameElement gameElement, KeyType keyType) {
		this.gameElement = gameElement;
		this.keyType = keyType;
	}

	@Override
	public void execute() {
		prevX = gameElement.getX();
		prevY = gameElement.getY();
		
		switch(keyType) {
		case RIGHT:
			gameElement.setVelX(Math.abs(gameElement.getInitialvelX()));
			gameElement.setVelY(0);
			break;
		case LEFT:
			gameElement.setVelX(-1 * Math.abs(gameElement.getInitialvelX()));
			gameElement.setVelY(0);
			break;
		case UP:
			gameElement.setVelY(-1 * Math.abs(gameElement.getInitialvelY()));
			gameElement.setVelX(0);
			break;
		case DOWN:
			gameElement.setVelY(Math.abs(gameElement.getInitialvelY()));
			gameElement.setVelX(0);
			break;
		}
	}

	@Override
	public void undo() {
		gameElement.setX(prevX);
		gameElement.setY(prevY);
	}
}
