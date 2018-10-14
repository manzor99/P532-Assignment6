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

class BoxLayoutYAxisBehaviorTest {
	
	GameElement element;
	AbstractPanel abstractPanel;
	KeyType keytype;
	BoxLayoutYAxisBehavior boxLayoutYAxisBehavior;
	
	
	@BeforeEach
	void setup() throws Exception{
		boxLayoutYAxisBehavior = new BoxLayoutYAxisBehavior();
		element = mock(GameElement.class);
		abstractPanel = mock(AbstractPanel.class);
		
	}
		
	@Test
	void updateLayoutBehaviorTest() {
		
		boxLayoutYAxisBehavior.updateLayoutBehavior(abstractPanel, 100, 100);
		verify(abstractPanel).getComponents();

	}

}