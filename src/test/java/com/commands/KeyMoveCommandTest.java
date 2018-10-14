package com.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.components.GameElement;
import com.infrastructure.KeyType;

import static org.mockito.Mockito.*;

class KeyMoveCommandTest {
	
	GameElement element;
	
	KeyType keytype;
	
	KeyMoveCommand keyMoveCommand;
	KeyMoveCommand keyMoveCommand1;
	KeyMoveCommand keyMoveCommand2;
	KeyMoveCommand keyMoveCommand3;
	
	@BeforeEach
	void setup() throws Exception{
		element = mock(GameElement.class);
		KeyType keyType = KeyType.LEFT;
		KeyType keyType1 = KeyType.RIGHT;
		KeyType keyType2 = KeyType.UP;
		KeyType keyType3 = KeyType.DOWN;
		keyMoveCommand = new KeyMoveCommand(element, keyType);
		keyMoveCommand1 = new KeyMoveCommand(element, keyType1);
		keyMoveCommand2 = new KeyMoveCommand(element, keyType2);
		keyMoveCommand3 = new KeyMoveCommand(element, keyType3);
	}
	
	@Test
	void executeTest() {
		keyMoveCommand.execute();
		when(element.getX()).thenReturn(60);
		verify(element).setVelX(anyInt());
	}
	
	@Test
	void executeTest1() {
		keyMoveCommand1.execute();
		when(element.getX()).thenReturn(60);
		verify(element).setVelX(anyInt());
	}
	
	@Test
	void executeTest2() {
		keyMoveCommand2.execute();
		when(element.getX()).thenReturn(60);
		verify(element).setVelX(anyInt());
	}
	
	@Test
	void executeTest3() {
		keyMoveCommand3.execute();
		when(element.getX()).thenReturn(60);
		verify(element).setVelX(anyInt());
	}

	@Test
	void testUndo() {
		keyMoveCommand.undo();
		verify(element).setX(anyInt());
		verify(element).setY(anyInt());
	}

}
