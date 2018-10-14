package com.timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;
import org.apache.log4j.Logger;

import com.infrastructure.Constants;
import com.infrastructure.Observable;
import com.infrastructure.Observer;


public class GameTimer implements Observable{
	protected static Logger log = Logger.getLogger(GameTimer.class);
	private Timer timer;
	ArrayList<Observer> observers = new ArrayList<>();

	public GameTimer(){
				
        ActionListener actionPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               notifyObservers();
            }
        };
         
        timer = new Timer(Constants.TIMER_COUNT ,(ActionListener) actionPerformer);
//        ABHI:: Timer started in constructor and also started from Main (GameMaker.java)
//        startTimer();
	}
	
	@Override
	public void registerObserver(Observer o) {
		
		// TODO Auto-generated method stub
		if(!observers.contains(o))
			observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(observers.indexOf(o));
	}

	@Override
	public void notifyObservers() {
		for(Observer observer: observers) {
			observer.update();
		}
	}
	
	public boolean isObserverListEmpty() {
		if(this.observers.isEmpty()) return true;
		return false;
	}
	
	public void startTimer() {
		System.out.println("Timer started");
		timer.start();
	}
	
	public void stopTimer() {
		timer.stop();
	}
}
