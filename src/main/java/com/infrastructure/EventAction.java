package com.infrastructure;

public class EventAction {
	private EventType event;
	private ActionType action;
	private int interval;
	
	public EventAction(EventType event, ActionType action, int interval) {
		this.event = event;
		this.action = action;
		this.interval = interval;
	}

	public EventType getEvent() {
		return event;
	}

	public ActionType getAction() {
		return action;
	}

	public int getInterval() {
		return interval;
	}
}
