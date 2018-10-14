package com.behavior;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.components.GameElement;
import com.infrastructure.KeyType;
import com.ui.AbstractPanel;

import static org.mockito.Mockito.*;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

class FlowLayoutBehaviorTest {
	
	GameElement element;
	AbstractPanel abstractPanel;
	KeyType keytype;
	FlowLayoutBehavior flowLayoutBehavior;
	
	
	@BeforeEach
	void setup() throws Exception{
		flowLayoutBehavior = new FlowLayoutBehavior();
		element = mock(GameElement.class);
		abstractPanel = mock(AbstractPanel.class);
		
	}
		
	@Test
	void updateLayoutBehaviorTest() {
		
		flowLayoutBehavior.updateLayoutBehavior(abstractPanel, 100, 100);
		verify(abstractPanel).setPreferredSize(any());

	}

}