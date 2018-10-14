package com.commands;

import com.components.GameElement;
import com.infrastructure.Command;

public class BounceCommand implements Command{
	private GameElement gameElement;
	
	public BounceCommand(GameElement gameElement) {
		this.gameElement = gameElement;
	}

	@Override
	public void execute() {
		gameElement.setVelX(-1 * gameElement.getVelX());
		gameElement.setVelY(-1 * gameElement.getVelY());
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
}
