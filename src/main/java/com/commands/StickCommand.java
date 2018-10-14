package com.commands;

import org.apache.log4j.Logger;

import com.components.GameElement;
import com.infrastructure.Command;

public class StickCommand implements Command{
	public static final Logger logger = Logger.getLogger(ChangeVelXCommand.class);
	private GameElement gameElement;
	private int velX, velY;
	
	public StickCommand(GameElement gameElement, int velX, int velY) {
		this.gameElement = gameElement;
		this.velX = velX;
		this.velY = velY;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		System.out.println("Before setting In Stick Command: " + gameElement.getVelX() + " vel Y : " + gameElement.getVelY());
//		
//		gameElement.setVelX(velX);
//		gameElement.setVelY(velY);
		
		
		gameElement.setX(gameElement.getX() + velX);
		gameElement.setY(gameElement.getY() + velY);
		
		System.out.println("After In Stick Command: " + gameElement.getVelX() + " vel Y : " + gameElement.getVelY());
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}

}
