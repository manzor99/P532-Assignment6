package com.commands;

import org.apache.log4j.Logger;

import com.components.GameElement;
import com.infrastructure.Command;


public class RandomMoveCommand implements Command{

	public static final Logger logger = Logger.getLogger(RandomMoveCommand.class);
	private GameElement element;
	private int min = 0;
	private int max = 2;
	private int randomNum;
	
	public RandomMoveCommand(GameElement element) {
		this.element = element;
	}
	
	@Override
	public void execute() {
		randomNum = (int)(Math.random()*((max-min)+1))+min;
		System.out.print(randomNum);
		element.setX(element.getX() - 2* element.getVelX());
		element.setY(element.getY() - 2* element.getVelY());
		int initialVelX = element.getVelX();
		int initialVelY = element.getVelY();
		
		switch(randomNum) {
		case 0:
				element.setVelX(-1 * initialVelX);
				element.setVelY(-1 * initialVelY);
			break;
		
		case 1:
			element.setVelX(-1 * initialVelY);
			element.setVelY(-1 * initialVelX);
			break;
			
		case 2:
				element.setVelX(initialVelY);
				element.setVelY(initialVelX);
			break;
		}

	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}


}
