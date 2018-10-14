package com.springboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.awt.Toolkit;
import com.gamemaker.GameMaker;

@RestController
public class SpringController {

	@RequestMapping("/games")
	public String games() {
		// TODO: make it so the user can load different games from this page
		return "No games yet";
	}

	@RequestMapping("/highscores")
	public String highScores() {
		// TODO: display high scores on this page
		return "No highscores yet";
	}

	@RequestMapping("/gameMaker")
	public void gameMaker() {
		GameMaker.runGame();
	}

}
