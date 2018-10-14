package com.controller;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.components.Clock;
import com.components.GameElement;
import com.components.ScoreBoard;
import com.dimension.Coordinate;
import com.dimension.Dimensions;
import com.helper.Collider;
import com.helper.CollisionChecker;
import com.infrastructure.ActionLink;
import com.infrastructure.ActionType;
import com.infrastructure.EventAction;
import com.infrastructure.EventType;
import com.infrastructure.KeyType;
import com.timer.GameTimer;
import com.ui.ControlPanel;
import com.ui.CustomButton;
import com.ui.DesignPanel;
import com.ui.GUI;
import com.ui.GamePanel;
import com.ui.PreviewPanel;
import com.ui.SpriteProperties;
import com.infrastructure.Command;
import com.infrastructure.Direction;

public class MainControllerTest {

	@Mock private GameElement element;
	@Mock private SpriteProperties spriteProperties;
	@Mock private GUI gui;
	@Mock private PreviewPanel previewPanel;
	@Mock private DesignPanel designPanel;
	@Mock private ControlPanel controlPanel;
	@Mock private DesignController designController;
	@Mock private GamePanel gamePanel;
	@Mock private GameTimer timer;
	@Mock private CollisionChecker collisionChecker;
	@Mock private Clock clock;
	@Mock private ScoreBoard scoreBoard;
	@Spy private ActionLink link = new ActionLink(element, ActionType.MOVE, 0);
	private List<EventAction> eventActions;
	@InjectMocks private MainController mainController;
	
	@BeforeEach
	void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		mainController = new MainController(gui, timer, designController, collisionChecker);
	}
	
	@Test
	void createActionCommand_MOVE() { 
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		when(link.getGameElement()).thenReturn(element);
		mainController.createActionCommand(link, KeyType.LEFT, 0);
		verify(link, times(2)).getGameElement();
		verify(link, times(1)).getAction();
		verify(link, times(1)).getInterval();
	}
	
	
	@Test
	void createActionCommand_JUMP() { 
		ActionLink link = new ActionLink(element, ActionType.JUMP, 0);
		ActionLink spyLink = Mockito.spy(link); 
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		mainController.createActionCommand(spyLink, KeyType.W, 0);
		verify(spyLink, times(1)).getGameElement();
		verify(spyLink, times(1)).getAction();
		verify(spyLink, times(1)).getInterval();
	}
	
	@Test
	void createActionCommand_RANDOM() { 
		ActionLink link = new ActionLink(element, ActionType.RANDOM, 0);
		ActionLink spyLink = Mockito.spy(link); 
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		mainController.createActionCommand(spyLink, null, 0);
		verify(spyLink, times(2)).getGameElement();
		verify(spyLink, times(1)).getAction();
		verify(spyLink, times(1)).getInterval();
	}
	
	@Test
	void createActionCommand_EXPLODE() { 
		ActionLink link = new ActionLink(element, ActionType.EXPLODE, 0);
		ActionLink spyLink = Mockito.spy(link); 
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		mainController.createActionCommand(spyLink, null, 0);
		verify(spyLink, times(1)).getGameElement();
		verify(spyLink, times(1)).getAction();
		verify(spyLink, times(1)).getInterval();
	}
	
	@Test
	void createActionCommand_BOUNCE() { 
		ActionLink link = new ActionLink(element, ActionType.BOUNCE, 0);
		ActionLink spyLink = Mockito.spy(link); 
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		mainController.createActionCommand(spyLink, null, 0);
		verify(spyLink, times(1)).getGameElement();
		verify(spyLink, times(1)).getAction();
		verify(spyLink, times(1)).getInterval();
	}
	
	@Test
	void gameElementCreator() {
		GameElement dummyBullet = new GameElement(new Dimensions(5, 20),
				new Coordinate(1000, 1000), "Bullet", 0, 10, "Rectangle");
		GameElement spyBullet = Mockito.spy(dummyBullet);
		List<GameElement> g = new ArrayList<>();
		g.add(spyBullet);
		when(designController.getGraphicsElements()).thenReturn(g);
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		when(gui.getGamePanel()).thenReturn(gamePanel);
		
		Collider collider = new Collider(spyBullet, element, ActionType.EXPLODE, ActionType.EXPLODE, collisionChecker, new ArrayList<Command>());
		List<Collider> listCollider = new ArrayList<>();
		listCollider.add(collider);
		when(designController.getDynamicColliderList()).thenReturn(listCollider);
		when(designController.getColliders()).thenReturn(listCollider);
		
		GameElement cloned = mainController.gameElementCreator(element);
		
		verify(spyBullet, times(2)).getName();
		verify(gamePanel, times(1)).addComponent(cloned);
	}
		
	
	@Test
	void update() {
		when(designController.getClock()).thenReturn(clock);
		when(designController.getColliders()).thenReturn(new ArrayList<Collider>());
		List<GameElement> g = new ArrayList<>();
		g.add(element);
		when(designController.getGraphicsElements()).thenReturn(g);
		when(collisionChecker.checkCollisionBetweenGameElementAndBounds(element)).thenReturn(Direction.X);
		
		ActionLink link = new ActionLink(element, ActionType.BOUNCE, 0);
		HashMap<EventType, ConcurrentLinkedQueue<ActionLink>> eventMap = new HashMap<>();
		eventMap.put(EventType.ON_TIMER_TICK, new ConcurrentLinkedQueue<ActionLink>());
		eventMap.get(EventType.ON_TIMER_TICK).add(link);
		when(designController.getEventMap()).thenReturn(eventMap);
		when(designController.getScoreBoard()).thenReturn(scoreBoard);
		
		
		mainController.update();
	}
}
