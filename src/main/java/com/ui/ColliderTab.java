package com.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.components.GameElement;
import com.controller.DesignController;
import com.helper.Collider;
import com.helper.CollisionChecker;
import com.infrastructure.ActionType;
import com.infrastructure.Constants;

@SuppressWarnings("serial")
public class ColliderTab extends JPanel {

	JScrollPane scroller;
	private JButton addColliderButton;
	private JButton colliderConfirm;
	private JPanel colliderButtons;
	DesignPanel design;
	DesignController designController;
	JComboBox primaryBox;
	JComboBox secondBox;
	PreviewPanel preview;
	private JComboBox<ActionType> collisionTypes;
	private JComboBox<ActionType> collisionTypes2;
	CollisionChecker collisionChecker = new CollisionChecker();
	List<GameElement> gameElements;

	public ColliderTab(DesignPanel design) {
		scroller = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		colliderConfirm = new JButton("colliderComfirm");
		colliderButtons = new JPanel(new FlowLayout());
		gameElements = new ArrayList<>();
		this.design = design;
	}

	public void addCollideButtons() {

		addColliderButton = new JButton("Add Collider");
		addColliderButton.setEnabled(design.isFinished());
		addColliderButton.addActionListener(design);
		addColliderButton.setActionCommand("addCollider");
		colliderButtons.add(addColliderButton);
		this.add(colliderButtons);
	}

	public void addColliderConfirmButton() {
		// for collider
		colliderConfirm.setActionCommand("colliderConfirm");
		colliderConfirm.setVisible(true);
		colliderConfirm.setAlignmentY(TOP_ALIGNMENT);
		colliderConfirm.addActionListener(design);
		// ABHI:: added to colliderButtons panel
//		collider.add(colliderComfire);
		colliderButtons.add(colliderConfirm);
	}

	public void addCollider(DesignController designController,
			PreviewPanel preview) {
		// TODO Auto-generated method stub
		this.designController = designController;
		this.preview = preview;
		gameElements = designController.getGraphicsElements();
		JPanel card = this;

		JPanel colliderCard = new JPanel(); // use FlowLayout
		colliderCard.setLayout(new BoxLayout(colliderCard, BoxLayout.Y_AXIS));
		colliderCard.setMaximumSize(
				new Dimension(Constants.DESIGN_PANEL_WIDTH, 125));

		ArrayList<String> names = new ArrayList<>();
		for (GameElement e : gameElements) {
			if (!names.contains(e.getName())) {
				names.add(e.getName());
			}
		}

		JPanel primary = new JPanel(); // use FlowLayout
		primary.add(new JLabel("Primary Object: ", JLabel.LEFT));
		primaryBox = new JComboBox<>(names.toArray());
		// String name = names.get(nameIndex);
		primary.add(primaryBox);
		colliderCard.add(primary);

		JPanel secondary = new JPanel(); // use FlowLayout
		secondary.add(new JLabel("Secondary Object: ", JLabel.LEFT));
		// names.remove(nameIndex);
		secondBox = new JComboBox<>(names.toArray());
		secondary.add(secondBox);
		colliderCard.add(secondary);

		JPanel collisionType = new JPanel(); // use FlowLayout
		collisionType.add(new JLabel("Primary Collision Type: ", JLabel.LEFT));
		collisionTypes = new JComboBox<>(ActionType.values());
		collisionType.add(collisionTypes);
		colliderCard.add(collisionType);

		JPanel collisionType2 = new JPanel(); // use FlowLayout
		collisionType2.add(new JLabel("Primary Collision Type: ", JLabel.LEFT));
		collisionTypes2 = new JComboBox<>(ActionType.values());
		collisionType2.add(collisionTypes2);
		colliderCard.add(collisionType2);

		Border redline = BorderFactory.createLineBorder(Color.gray);
		TitledBorder border = BorderFactory.createTitledBorder(redline,
				"Collider " + (design.getColliders().size() + 1));
		border.setTitleJustification(TitledBorder.LEFT);
		border.setTitlePosition(TitledBorder.BELOW_TOP);
		preview.setBorder(redline);
		colliderCard.setBorder(border);
		card.add(colliderCard);

		this.revalidate();
		this.repaint();
	}

	public void collidersFinished(DesignController designController) {
		this.designController = designController;

		List<GameElement> gameElements = new ArrayList<>();
		gameElements = designController.getGraphicsElements();
		
		 String primaryItem = (String) primaryBox.getSelectedItem();
		 String secondaryItem = (String) secondBox.getSelectedItem();

		for(int index = 0; index < gameElements.size(); index++) {
			if(gameElements.get(index).getName().equals(primaryItem)) {
				for(int j = 0; j<gameElements.size(); j++) {
					if(gameElements.get(j).getName().equals(secondaryItem)) {
						 Collider collider = new Collider(
								 gameElements.get(index), gameElements.get(j),
								 (ActionType) collisionTypes.getSelectedItem(), 
								 (ActionType) collisionTypes2.getSelectedItem(),
								 collisionChecker, design.getEventList());
						 
						 if(collider.getPrimaryElement().getName().equals("Bullet") || collider.getSecondaryElement().getName().equals("Bullet")) {
							 designController.getDynamicColliderList().add(collider);
						 }
						 else {
							 designController.addGameColliders(collider);
						 }
							
					}
				}
			}

		}
	}

}
