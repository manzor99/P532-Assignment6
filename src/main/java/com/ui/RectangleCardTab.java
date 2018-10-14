package com.ui;

import java.awt.event.ItemEvent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class RectangleCardTab extends AbstractCardType {

	public RectangleCardTab(DesignPanel designPanel, JFrame frame) {
		super(designPanel, frame, "Rectangle");
		remove(radiusLabel);
		remove(radius);
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

