package com.commands;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.util.List;

import javax.swing.Timer;

import com.components.GameElement;
import com.infrastructure.Command;
import com.infrastructure.Constants;
import com.infrastructure.GameElementShape;
import com.infrastructure.KeyType;
import com.helper.Collider;

public class KeyJumpCommand implements Command{
	GameElement gameElement;
	private KeyType keyType;
	private List<Collider> colliderList;
	int counter = 10; 
	Timer timer;
	
	public KeyJumpCommand(GameElement gameElement, KeyType keyType, List<Collider> colliderList) {
		this.gameElement = gameElement;
		this.keyType = keyType;
		this.colliderList = colliderList;
	}
	
	public boolean checkIntersectionBetweenElements(GameElement element1,
			GameElement element2) {

		RectangularShape shape1 = getDims(element1);
		RectangularShape shape2 = getDims(element2);

		Area areaA = new Area(shape1);
		areaA.intersect(new Area(shape2));
		return !areaA.isEmpty();
	}

	public RectangularShape getDims(GameElement element) {
		if (element.getGameElementShape() == GameElementShape.RECTANGLE)
			return new Rectangle(element.getX(), element.getY(),
					element.getWidth(), element.getHeight());
		else
			return new Ellipse2D.Double(element.getX(), element.getY(),
					element.getWidth(), element.getHeight());
	}

	@Override
	public void execute() {
		gameElement.setInAir(true);
		ActionListener actionPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
   			   	if(counter == 0) {
   			   	boolean intersecting = false;
//   			   	System.out.print(colliderList.toString());
   			   		for(Collider collider: colliderList) {
   			   			
   			   			if(collider.getPrimaryElement().getName().equals(gameElement.getName())) {
   			   				 intersecting = checkIntersectionBetweenElements(gameElement, collider.getSecondaryElement());
//   			   				 System.out.println("Intersecting in primary: "+ intersecting);
   			   				 if(intersecting) {break;}
   			   				
   			   			} else if(collider.getSecondaryElement().getName().equals(gameElement.getName())) {
   			   			 intersecting = checkIntersectionBetweenElements(gameElement, collider.getPrimaryElement());
//   			   		System.out.println("Intersecting in secondary: "+ intersecting);
   			   			 	if(intersecting) { break;}
   			   			}
   			   			
   			   		}
   			   		
   			   		System.out.println(intersecting);
   			   		if(!intersecting) {
   			   			ExplodeCommand explodeCommand = new ExplodeCommand(gameElement);
   			   			explodeCommand.execute();
   			   		}
//   			   		gameElement.setVelX(gameElement.getInitialvelX());
//   			   		gameElement.setVelY(gameElement.getInitialvelY());
   			   		timer.stop();
   			   		gameElement.setInAir(false);
            	}
   			   	switch(keyType) {
   			   	case W:
   			   		gameElement.setY(gameElement.getY()-gameElement.getInitialvelY());
   			   		counter -= 1;
   			   		break;
   			   	case S:
   			   		gameElement.setY(gameElement.getY()+gameElement.getInitialvelY());
   			   		counter -= 1;
   			   		break;
   			   	default: 
   			   		break;
   			   	
   			   	}
   			   	
            }
        };
        this.timer = new Timer(50,(ActionListener) actionPerformer);
        timer.start();
	}

	@Override
	public void undo() {
		
		
	}
	
}
