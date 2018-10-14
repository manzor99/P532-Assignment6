package com.commands;

import org.apache.log4j.Logger;

import com.components.GameElement;
import com.infrastructure.Command;
import com.infrastructure.MoveType;

public class MoveCommand implements Command {

	public static final Logger logger = Logger.getLogger(MoveCommand.class);
	private int prevX;
	private int prevY;
	private int prevVelX;
	private int prevVelY;
	private GameElement element;

	public MoveCommand(GameElement element) {
		this.element = element;

	}

	@Override
	public void execute() {
		prevX = element.getX();
		prevY = element.getY();
		prevVelX = element.getVelX();
		prevVelY = element.getVelY();

		element.setX(element.getX() + element.getVelX());
		element.setY(element.getY() + element.getVelY());
	}

	@Override
	public void undo() {
		element.setX(prevX);
		element.setY(prevY);
		element.setVelX(prevVelX);
		element.setVelY(prevVelY);
	}

}
