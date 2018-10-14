package com.ui;

import java.awt.event.ItemEvent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class CircleCardTab extends AbstractCardType {

	public CircleCardTab(DesignPanel designPanel, JFrame frame) {
		super(designPanel, frame, "Circle");
		remove(widthLabel);
		remove(heightLabel);
		remove(width);
		remove(height);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		itemStateChanged(e);
	}

	public SpriteProperties getSpriteProperties() {
		return super.getSpriteProperties();
	}

}

