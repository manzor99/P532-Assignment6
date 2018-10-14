package com.commands;

import org.apache.log4j.Logger;

import com.components.GameElement;
import com.infrastructure.Command;

public class ResetCommand implements Command {

	public static final Logger logger = Logger.getLogger(ExplodeCommand.class);
	private GameElement gameElement;

	public ResetCommand(GameElement gameElement) {
		this.gameElement = gameElement;
	}

	@Override
	public void execute() {
		gameElement.reset();
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

}
