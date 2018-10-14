package com.infrastructure;

import java.io.Serializable;

import com.components.GameElement;



public class ActionLink implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ActionType action;
	GameElement gameElement;
	int interval;	
	
	public ActionType getAction() {
		return action;
	}

	public GameElement getGameElement() {
		return gameElement;
	}

	public int getInterval() {
		return interval;
	}

	public ActionLink(GameElement gameElement, ActionType action, int interval){
		this.gameElement = gameElement;
		this.action = action;
		this.interval = interval;
	}
}