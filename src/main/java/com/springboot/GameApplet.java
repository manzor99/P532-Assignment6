package com.springboot;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JPanel;

public class GameApplet extends JApplet {

	public GameApplet() {
		super();
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(500, 500));
		panel.setBackground(Color.BLUE);
		this.setContentPane(panel);
	}

}
