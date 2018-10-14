package com.ui;

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
import javax.swing.JPanel;

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
import com.controller.DesignController;
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

public class ColliderTabTest {

	@Mock private GameElement element;
	@Mock private SpriteProperties spriteProperties;
	@Mock private GUI gui;
	@Mock private PreviewPanel previewPanel;
	@Mock private DesignPanel designPanel;
	@Mock private ControlPanel controlPanel;
	@Mock private GamePanel gamePanel;
	@Mock private GameTimer timer;
	@Mock private CollisionChecker collisionChecker;
	@Mock private Clock clock;
	@Mock private DesignController designController;
	@Mock private ScoreBoard scoreBoard;
	@Mock private JPanel colliderButtons;
	@Mock private JButton addColliderButton;
	@Spy private ActionLink link = new ActionLink(element, ActionType.MOVE, 0);
	private List<EventAction> eventActions;
	@InjectMocks private ColliderTab colliderTab;
	
	@BeforeEach
	void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		colliderTab = new ColliderTab(designPanel);
	}
	
	@Test
	void addColliderTest() { 
		colliderTab.addCollider(designController, previewPanel);
		verify(previewPanel, times(1)).setBorder(any());
	}
	
}