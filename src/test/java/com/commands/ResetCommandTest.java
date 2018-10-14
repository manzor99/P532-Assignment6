package com.commands;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.components.GameElement;

public class ResetCommandTest {

	@Mock private GameElement element;
	
	@InjectMocks private ResetCommand resetCommand;
	
	@BeforeEach
	void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void ShouldChangeElementDirectionWhenExecuteCalled() {
		resetCommand.execute();
		
		verify(element).reset();
	}
		
}