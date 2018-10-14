package com.controller;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import com.components.GameElement;
import com.infrastructure.ActionType;
import com.infrastructure.EventAction;
import com.infrastructure.EventType;
import com.ui.ControlPanel;
import com.ui.CustomButton;
import com.ui.DesignPanel;
import com.ui.GUI;
import com.ui.GamePanel;
import com.ui.PreviewPanel;
import com.ui.SpriteProperties;

public class DesignControllerTest {

	@Mock private GameElement element;
	@Mock private SpriteProperties spriteProperties;
	@Mock private GUI gui;
	@Mock private PreviewPanel previewPanel;
	@Mock private DesignPanel designPanel;
	@Mock private ControlPanel controlPanel;
	@Mock private MainController mainController;
	@Mock private GamePanel gamePanel;
	private List<EventAction> eventActions;
	@InjectMocks private DesignController designController;
	
	@BeforeEach
	void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		designController = new DesignController(gui);
		eventActions = new ArrayList<>();
		eventActions.add(new EventAction(EventType.ON_TIMER_TICK, ActionType.BOUNCE, 0));
	}
	
	@Test
	void setSpriteProperties_cicle() {
	
		when(spriteProperties.getType()).thenReturn("Circle");
		when(spriteProperties.getQuantity()).thenReturn(1);
		when(spriteProperties.getImage()).thenReturn(null);
		when(spriteProperties.getColor()).thenReturn(Color.BLACK);
		when(spriteProperties.isGameEnd()).thenReturn(true);
		when(spriteProperties.isScoreElement()).thenReturn(true);
		when(spriteProperties.getEventAction()).thenReturn(eventActions);
		when(gui.getDesignPanel()).thenReturn(designPanel);
		when(designPanel.getPreview()).thenReturn(previewPanel);
		designController.setSpriteProperties(spriteProperties);
		
		verify(spriteProperties, times(0)).getWidth();
		verify(spriteProperties, times(1)).getRadius();
	}
	@Test
	void setSpriteProperties_rectangle() {
		
		when(spriteProperties.getType()).thenReturn("Rectangle");
		when(spriteProperties.getQuantity()).thenReturn(1);
		when(spriteProperties.getImage()).thenReturn(null);
		when(spriteProperties.getColor()).thenReturn(Color.BLACK);
		when(spriteProperties.isGameEnd()).thenReturn(true);
		when(spriteProperties.isScoreElement()).thenReturn(true);
		when(gui.getDesignPanel()).thenReturn(designPanel);
		when(designPanel.getPreview()).thenReturn(previewPanel);
		designController.setSpriteProperties(spriteProperties);
		
		verify(spriteProperties, times(1)).getWidth();
		verify(spriteProperties, times(0)).getRadius();
	}
	
	@Test
	void addControlElement() {
		when(gui.getDesignPanel()).thenReturn(designPanel);
		when(designPanel.getControlElement()).thenReturn(new CustomButton("Play", "play", 20, 50, mainController));
		when(gui.getControlPanel()).thenReturn(controlPanel);
		designController.addControlElement();
		
		verify(controlPanel, times(1)).addButtons(any());
	}
	
	@Test
	void mouseClicked() {
		MouseEvent mouseEvent = new MouseEvent(new JButton("PLay"), 0, 0, 0, 5, 10, 0, false);
		when(gui.getGamePanel()).thenReturn(gamePanel);
		designController.mouseClicked(mouseEvent);
		verify(gamePanel, times(1)).addComponent(any());
		assertEquals(designController.getGraphicsElements().get(0).getX(), 5);
		assertEquals(designController.getGraphicsElements().get(0).getY(), 10);
	}
		
}
