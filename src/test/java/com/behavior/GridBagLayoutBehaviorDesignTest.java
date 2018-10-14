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

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

class GridBagLayoutBehaviorDesignTest {
	
	GameElement element;
	AbstractPanel abstractPanel;
	KeyType keytype;
	GridBagLayoutBehaviorDesign gridBagLayoutBehaviorDesign;
	Component comp;
	
	@BeforeEach
	void setup() throws Exception{
		gridBagLayoutBehaviorDesign = new GridBagLayoutBehaviorDesign();
		element = mock(GameElement.class);
		abstractPanel = mock(AbstractPanel.class);
		comp = mock(Component.class);
		
	}
		
	@Test
	void updateLayoutBehaviorTest() {
		ArrayList<Component> s = new ArrayList<Component>();
		s.add(new JLabel());
		when(abstractPanel.getComponents()).thenReturn(s.toArray(new JLabel[0]));
		gridBagLayoutBehaviorDesign.updateLayoutBehavior(abstractPanel, 100, 100);
		verify(abstractPanel).setPreferredSize(any());

	}

}