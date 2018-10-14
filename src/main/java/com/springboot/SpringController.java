package com.springboot;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public JPanel gameMaker() {
		// GameMaker.runGame();
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 200));
		panel.setBackground(Color.BLUE);
		return panel;
	}

}
