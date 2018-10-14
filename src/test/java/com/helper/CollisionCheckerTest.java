package com.helper;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.components.GameElement;
import com.dimension.Coordinate;
import com.infrastructure.Direction;
import com.infrastructure.GameElementShape;

public class CollisionCheckerTest {

	@Mock private GameElement element;
	
	@InjectMocks private CollisionChecker collisionChecker;
	
	@BeforeEach
	void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		collisionChecker = new CollisionChecker();
	}
	
	@Test
	void ShouldChangeElementDirectionWhenExecuteCalled() {
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		when(element.getWidth()).thenReturn(10);
		when(element.getHeight()).thenReturn(10);
		assertEquals(Direction.NONE, collisionChecker.checkCollisionBetweenGameElementAndBounds(element));
	}
	 
	@Test
	void ShouldChangeElementDirectionWhenExecuteCalled_1() {
		when(element.getX()).thenReturn(-10);
		when(element.getY()).thenReturn(10);
		when(element.getWidth()).thenReturn(10);
		when(element.getHeight()).thenReturn(10);
		when(element.getCoordinate()).thenReturn(new Coordinate(-10,10));
		assertEquals(Direction.X, collisionChecker.checkCollisionBetweenGameElementAndBounds(element));
	}
	
	@Test
	void ShouldChangeElementDirectionWhenExecuteCalled_2() {
		when(element.getX()).thenReturn(750);
		when(element.getY()).thenReturn(10);
		when(element.getWidth()).thenReturn(10);
		when(element.getHeight()).thenReturn(10);
		when(element.getCoordinate()).thenReturn(new Coordinate(10,10));
		assertEquals(Direction.X, collisionChecker.checkCollisionBetweenGameElementAndBounds(element));
	}
	 
	@Test
	void ShouldChangeElementDirectionWhenExecuteCalled_3() {
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(-10);
		when(element.getWidth()).thenReturn(10);
		when(element.getHeight()).thenReturn(10);
		when(element.getCoordinate()).thenReturn(new Coordinate(10,-10));
		assertEquals(Direction.Y, collisionChecker.checkCollisionBetweenGameElementAndBounds(element));
	}
	
	@Test
	void ShouldChangeElementDirectionWhenExecuteCalled_4() {
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(1000);
		when(element.getWidth()).thenReturn(10);
		when(element.getHeight()).thenReturn(10);
		when(element.getCoordinate()).thenReturn(new Coordinate(10,10));
		assertEquals(Direction.Y, collisionChecker.checkCollisionBetweenGameElementAndBounds(element));
	} 
	
	@Test
	void checkCollisionBetweenGameElements_element1_rectangle(){
		GameElement element2 = element;
		when(element.getGameElementShape()).thenReturn(GameElementShape.RECTANGLE);
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		when(element.getWidth()).thenReturn(10);
		when(element.getHeight()).thenReturn(10);
		when(element2.getX()).thenReturn(10);
		when(element2.getY()).thenReturn(10);
		when(element2.getWidth()).thenReturn(10);
		when(element2.getHeight()).thenReturn(10);
		collisionChecker.checkCollisionBetweenGameElements(element, element2);
		assertEquals(Direction.X, collisionChecker.checkCollisionBetweenGameElements(element, element2));
	}
	@Test
	void checkCollisionBetweenGameElements_element1_topleft1(){
		GameElement element2 = element;
		when(element.getGameElementShape()).thenReturn(GameElementShape.CIRCLE);
		when(element.getX()).thenReturn(10);
		when(element.getY()).thenReturn(10);
		when(element.getWidth()).thenReturn(10);
		when(element.getHeight()).thenReturn(10);
		when(element2.getX()).thenReturn(10);
		when(element2.getY()).thenReturn(0);
		when(element2.getWidth()).thenReturn(10);
		when(element2.getHeight()).thenReturn(10);
		collisionChecker.checkCollisionBetweenGameElements(element, element2);
		assertEquals(Direction.Y, collisionChecker.checkCollisionBetweenGameElements(element, element2));
	}	
}