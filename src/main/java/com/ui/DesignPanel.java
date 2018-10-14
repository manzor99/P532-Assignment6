/**
 *This class contains timer and control panel*/
package com.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.log4j.Logger;
import com.behavior.FlowLayoutBehavior;

import com.components.GameElement;
import com.controller.DesignController;
import com.controller.MainController;
import com.dimension.Coordinate;
import com.dimension.Dimensions;
import com.helper.Collider;
import com.helper.CollisionChecker;
import com.infrastructure.ButtonActionType;
import com.infrastructure.Command;
import com.infrastructure.Constants;
import com.infrastructure.Element;
import com.infrastructure.EventAction;
import com.infrastructure.MoveType;
import com.strategy.DrawOvalColor;
import com.strategy.DrawRectangularColorShape;
//import com.ui.EndingConditions;

@SuppressWarnings("serial")
public class DesignPanel extends AbstractPanel
		implements DocumentListener, Element, ItemListener, ActionListener {
	protected static Logger log = Logger.getLogger(DesignPanel.class);
	private MainController driver;
	private JTabbedPane tabbedPane;
	private DesignController designController;
	private JPanel graphic;
	private JPanel control;
	private JPanel timeLine;
	private JPanel cards;
	private boolean firstTime;
	private JPanel comboBoxPane;
	private boolean finished;
	private JFrame frame;
	private ArrayList<Element> elements;
	private ArrayList<Collider> colliders;
	final static String CIRCLE = "Circle Shape";
	final static String RECTANGLE = "Rectangle Shape";
	private CustomButton tendToAddButton;

	// timeline tag
	private JButton endingConfirm;
	private JPanel buttonBuildPanel;
	private JPanel controlElementPanel;
	//private EndingConditions end;
	private JButton addGraphicElementButton;
	private JButton finishedButton;
	private boolean pushedElement;
	JButton colliderConfirm;

	CollisionChecker collisionChecker = new CollisionChecker();
	ArrayList<Command> eventList = new ArrayList<>();
	ColliderTab collide;

	private List<EventAction> eventMapOval = new ArrayList<>();
	private List<EventAction> eventMapRect = new ArrayList<>();
	private PreviewPanel preview;

	private String selectedShape = "Circle";

	public DesignPanel() {
		this.firstTime = true;
		this.colliders = new ArrayList<>();
		setBorder("Design Center"); // Method call for setting the border
		setLayoutBehavior(new FlowLayoutBehavior());
		setBackground(Color.DARK_GRAY);
		this.finished = true;
		graphic = new JPanel();
		graphic.setBackground(Color.LIGHT_GRAY);
		graphic.setLayout(new BoxLayout(graphic, BoxLayout.Y_AXIS));

		collide = new ColliderTab(this);

		control = new JPanel();
		control.setBackground(Color.LIGHT_GRAY);
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));

		timeLine = new JPanel();
		timeLine.setBackground(Color.LIGHT_GRAY);
		timeLine.setLayout(new BoxLayout(timeLine, BoxLayout.Y_AXIS));

		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Graphic", null, graphic, null);
		tabbedPane.addTab("Control", null, control, null);
		tabbedPane.addTab("Colliders", null, collide.scroller, null);
		tabbedPane.setPreferredSize(
				new Dimension(Constants.DESIGN_PANEL_WIDTH, 500));
		ChangeListener changeListenerForTab = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				preview.removeAll();
				preview.setElements(new ArrayList<Element>());
				refresh(preview);
			}
		};
		tabbedPane.addChangeListener(changeListenerForTab);
		this.add(tabbedPane);

		// Create Preview Panel, which show the preview of the element
		preview = new PreviewPanel();
		Border redline = BorderFactory.createLineBorder(Color.red);
		TitledBorder border = BorderFactory.createTitledBorder(redline,
				"Preview Window");
		border.setTitleJustification(TitledBorder.LEFT);
		border.setTitlePosition(TitledBorder.BELOW_TOP);
		preview.setBorder(redline);
		preview.setBorder(border);
		preview.setPreferredSize(new Dimension(Constants.DESIGN_PANEL_WIDTH,
				Constants.PREVIEW_PANEL_HEIGHT));
		this.add(preview);

		// Update Layout
		performUpdateLayout(this, Constants.DESIGN_PANEL_WIDTH,
				Constants.DESIGN_PANEL_HEIGHT);
		elements = new ArrayList<>();

		// Init creates Add Element button
		init(null);
	}

	public void addEventActionOval(EventAction e) {
		this.eventMapOval.add(e);
	}

	public void addEventActionRect(EventAction e) {
		this.eventMapRect.add(e);
	}

	public void init(GameElement gameElement) {

		this.finished = gameElement == null;
		this.pushedElement = gameElement != null;

		// This button adds a new combo box to select basic shape of the
		// for graphic tab
		JPanel baseButtons = new JPanel(new FlowLayout());
		addGraphicElementButton = new JButton("Add Element");
		addGraphicElementButton.setEnabled(this.finished);
		addGraphicElementButton.addActionListener(this);
		addGraphicElementButton.setActionCommand("addElement");

		finishedButton = new JButton("Finished");
		finishedButton.setEnabled(!this.finished);
		finishedButton.addActionListener(this);
		finishedButton.setActionCommand("finishedElement");

		baseButtons.add(addGraphicElementButton);
		baseButtons.add(finishedButton);
		graphic.add(baseButtons);

		if (firstTime) {
			this.firstTime = false;
			collide.addCollideButtons();

			// control variable
			tendToAddButton = new CustomButton();

			buttonBuildPanel = new JPanel();
			buttonBuildPanel.setAlignmentX(LEFT_ALIGNMENT);

			controlElementPanel = new JPanel();
			controlElementPanel.setAlignmentX(LEFT_ALIGNMENT);
			controlElementPanel.setBackground(Color.LIGHT_GRAY);
			controlElementPanel.setLayout(
					new BoxLayout(controlElementPanel, BoxLayout.Y_AXIS));

			// for control tab
			JButton controlElementButton = new JButton("Button");
			controlElementButton.setFont(new Font("Times", Font.PLAIN, 12));
			controlElementButton.addActionListener(this);
			controlElementButton.setActionCommand("ElementButton");
			controlElementButton.setVisible(true);
			controlElementButton.setAlignmentX(LEFT_ALIGNMENT);
			controlElementButton.setAlignmentY(BOTTOM_ALIGNMENT);
			controlElementPanel.add(controlElementButton);
			controlElementPanel.add(Box.createRigidArea(new Dimension(5, 5)));

			control.add(controlElementPanel);

			collide.addColliderConfirmButton();
			// For timeline
			//end = new EndingConditions();
			JPanel buildConditionPanel = new JPanel();
			iniBuildConditionPanel(buildConditionPanel);
			timeLine.add(buildConditionPanel);
		}
		if (gameElement != null) {
			this.addElementSelect(gameElement);
		}

	}

	public void iniBuildConditionPanel(JPanel buildConditionPanel) {
		buildConditionPanel.setLayout(
				new BoxLayout(buildConditionPanel, BoxLayout.Y_AXIS));
		JCheckBox c1 = new JCheckBox("Timer");
		JCheckBox c2 = new JCheckBox("Score");
		JCheckBox c3 = new JCheckBox("Collision");
		c1.setActionCommand("timerCondition");
		c1.addActionListener(this);
		c2.setActionCommand("scoreCondition");
		c2.addActionListener(this);
		c3.setActionCommand("collisionCondition");
		c3.addActionListener(this);
		c1.setAlignmentY(LEFT_ALIGNMENT);
		c2.setAlignmentY(LEFT_ALIGNMENT);
		c3.setAlignmentY(LEFT_ALIGNMENT);

		JLabel timerlabel = new JLabel("Timer reachs : ");
		JTextField timerField = new JTextField("", 10);
		timerField.setName("timerField");
		timerField.getDocument().addDocumentListener(this);
		timerField.getDocument().putProperty("owner", timerField);

		JLabel scorelabel = new JLabel("Score reachs : ");
		JTextField scoreField = new JTextField("", 10);
		scoreField.setName("scoreField");
		scoreField.getDocument().addDocumentListener(this);
		scoreField.getDocument().putProperty("owner", scoreField);

		endingConfirm = new JButton("That is ending condition!");
		endingConfirm.setActionCommand("endingConfirm");
		endingConfirm.setVisible(true);
		JPanel timerFieldHolder = new JPanel();
		timerFieldHolder.add(c1);
		timerFieldHolder.add(timerlabel);
		timerFieldHolder.add(timerField);
		JPanel scoreFieldHolder = new JPanel();
		scoreFieldHolder.add(c2);
		scoreFieldHolder.add(scorelabel);
		scoreFieldHolder.add(scoreField);
		buildConditionPanel.add(endingConfirm);
		buildConditionPanel.add(timerFieldHolder);
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public void controlElementButtonSelect() {
		preview.removeAll();
		refresh(preview);
		control.remove(buttonBuildPanel);
		buttonBuildPanel.removeAll();
		ButtonActionType action = ButtonActionType.SAVE;
		tendToAddButton.setActionType(action);
		JLabel buttonNameLabel = new JLabel("Button Name : ");
		JTextField buttonName = new JTextField("", 15);
		buttonName.setName("buttonNameField");
		buttonName.getDocument().addDocumentListener(this);
		buttonName.getDocument().putProperty("owner", buttonName);

		JLabel buttonWidthLabel = new JLabel("Button Width : ");
		JTextField buttonWidth = new JTextField("", 15);
		buttonWidth.setName("buttonWidthField");
		buttonWidth.getDocument().addDocumentListener(this);
		buttonWidth.getDocument().putProperty("owner", buttonWidth);

		JLabel buttonHeightLabel = new JLabel("Button Height : ");
		JTextField buttonHeight = new JTextField("", 15);
		buttonHeight.setName("buttonHeightField");
		buttonHeight.getDocument().addDocumentListener(this);
		buttonHeight.getDocument().putProperty("owner", buttonHeight);

		JLabel buttonActionLabel = new JLabel("Button Action : ");
		JComboBox<ButtonActionType> boxAction = new JComboBox<>(
				ButtonActionType.values());
		boxAction.setName("boxAction");
		boxAction.setActionCommand("boxActionChanged");
		boxAction.addActionListener(this);

		buttonBuildPanel.add(buttonNameLabel);
		buttonBuildPanel.add(buttonName);
		buttonBuildPanel.add(buttonHeightLabel);
		buttonBuildPanel.add(buttonHeight);
		buttonBuildPanel.add(buttonWidthLabel);
		buttonBuildPanel.add(buttonWidth);
		buttonBuildPanel.add(buttonActionLabel);
		buttonBuildPanel.add(boxAction);
		control.add(buttonBuildPanel);
		tendToAddButton.setAlignmentY(CENTER_ALIGNMENT);
		preview.add(tendToAddButton);
		this.validate();
	}

	// This creates the separate views for circle and retangle
	public void addElementSelect(GameElement gameElement) {
		int index = 0;
		// Default / Initial Shape
		GameElement g = new GameElement(
				new Dimensions(Constants.PREVIEW_RADIUS),
				new Coordinate(Constants.PREVIEW_X_START,
						Constants.PREVIEW_Y_START),
				"null", 20, 0, "Oval");
		g.setColor(Color.BLACK);
		g.setDraw(new DrawOvalColor());
		g.setVisible(true);

		if (gameElement != null) {
			index = gameElement.getShapeType().equals("Oval") ? 0 : 1;
			g = gameElement;
		}
		this.preview.addGameElement(g);
		// Where the components controlled by the CardLayout are initialized:
		this.finished = false; // Prevents user adding another element until
								// finished
		this.addGraphicElementButton.setEnabled(this.finished);
		this.finishedButton.setEnabled(!this.finished);

		AbstractCardType circleCard = new CircleCardTab(this, frame);
		AbstractCardType rectangleCard = new RectangleCardTab(this, frame);

		// Create the panel that contains the "cards".
		cards = new JPanel(new CardLayout());

		cards.setPreferredSize(new Dimension(250, 200));
		cards.add(circleCard, CIRCLE);
		cards.add(rectangleCard, RECTANGLE);

		// Where the GUI is assembled:
		// Put the JComboBox in a JPanel to get a nicer look.
		comboBoxPane = new JPanel(); // use FlowLayout
		String comboBoxItems[] = { CIRCLE, RECTANGLE };
		JComboBox<String> cb = new JComboBox<>(comboBoxItems);
		cb.setEditable(false);
		cb.addItemListener(this);
		cb.setSelectedIndex(index);
		comboBoxPane.add(cb);
		graphic.add(comboBoxPane, BorderLayout.PAGE_START);
		graphic.add(cards, BorderLayout.CENTER);
		this.validate();

		this.preview.addGameElement(g);
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public void createButtons(MainController driver) {
		this.driver = driver;
		// get driver , add buttons for adding control element

		JButton controlElementTimer = new JButton("Add Timer");
		controlElementTimer.setFont(new Font("Times", Font.PLAIN, 12));
		controlElementTimer.addActionListener(driver);
		controlElementTimer.setActionCommand("AddTimer");
		controlElementTimer.setVisible(true);
		controlElementTimer.setAlignmentX(LEFT_ALIGNMENT);
		controlElementTimer.setAlignmentY(BOTTOM_ALIGNMENT);
		controlElementPanel.add(controlElementTimer);
		controlElementPanel.add(Box.createRigidArea(new Dimension(5, 5)));

		JButton controlElementScore = new JButton("Add Score");
		controlElementScore.setFont(new Font("Times", Font.PLAIN, 12));
		controlElementScore.addActionListener(driver);
		controlElementScore.setActionCommand("AddScore");
		controlElementScore.setVisible(true);
		controlElementScore.setAlignmentX(LEFT_ALIGNMENT);
		controlElementScore.setAlignmentY(BOTTOM_ALIGNMENT);
		controlElementPanel.add(controlElementScore);
		controlElementPanel.add(Box.createRigidArea(new Dimension(5, 5)));

		JButton addControlElementButton = new JButton("AddControlElement");
		addControlElementButton.addActionListener(driver);
		addControlElementButton.setActionCommand("AddControlElement");
		addControlElementButton.setVisible(true);
		addControlElementButton.setAlignmentX(LEFT_ALIGNMENT);
		addControlElementButton.setAlignmentY(BOTTOM_ALIGNMENT);
		controlElementPanel.add(addControlElementButton);
		controlElementPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		endingConfirm.addActionListener(driver);

	}

	// This changes the card shown for creating circle vs rectangle and changes
	// preview window
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getItem() == CIRCLE) {
			selectedShape = "Circle";
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.show(cards, (String) evt.getItem());
			this.preview.setElements(new ArrayList<Element>());
			GameElement g = new GameElement(
					new Dimensions(Constants.PREVIEW_RADIUS),
					new Coordinate(Constants.PREVIEW_X_START,
							Constants.PREVIEW_Y_START),
					"New", 20, 0, "Oval");
			g.setColor(Color.BLACK);
			g.setDraw(new DrawOvalColor());
			g.setVisible(true);
			this.preview.addGameElement(g);

		} else if (evt.getItem() == RECTANGLE) {
			selectedShape = "Rectangle";
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.show(cards, (String) evt.getItem());
			this.preview.setElements(new ArrayList<Element>());
			GameElement g = new GameElement(
					new Dimensions(Constants.PREVIEW_RADIUS * 2,
							Constants.PREVIEW_RADIUS * 2),
					new Coordinate(Constants.PREVIEW_X_START,
							Constants.PREVIEW_Y_START),
					"New", 20, 0, "Rectangle");
			g.setColor(Color.BLACK);
			g.setDraw(new DrawRectangularColorShape());
			g.setVisible(true);
			this.preview.addGameElement(g);
		}

		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addElement")) {
			this.addElementSelect(null);
		}
		if (e.getActionCommand().equals("ElementButton")) {
			this.controlElementButtonSelect();
		}
		if (e.getActionCommand().equals("finishedElement")) {
			this.pushToController();
		}
		if (e.getActionCommand().equals("boxActionChanged")) {
			JComboBox boxAction = (JComboBox) e.getSource();
			tendToAddButton.setActionType(ButtonActionType
					.valueOf(boxAction.getSelectedItem().toString()));
		}
		if (e.getActionCommand().equals("addCollider")) {
			collide.addCollider(designController, preview);
		}
		if (e.getActionCommand().equals("colliderConfirm")) {
			collide.collidersFinished(designController);
		}
	}

	private void pushToController() {
		AbstractCardType abstractCardType;
		if (selectedShape.equals("Circle")) {
			abstractCardType = (CircleCardTab) cards.getComponent(0);
		} else {
			abstractCardType = (RectangleCardTab) cards.getComponent(1);
		}
		designController
				.setSpriteProperties(abstractCardType.getSpriteProperties());
		abstractCardType.getEventMap().clear();
	}

	public void pushToPreview(GameElement temp) {
		try {
			this.graphic.removeAll();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		this.preview.addGameElement(temp);
		init(temp);
	}

	public void setBorder(String title) {
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(blackline,
				title);
		border.setTitleJustification(TitledBorder.LEFT);
		border.setTitleColor(Color.white);
		border.setTitlePosition(TitledBorder.BELOW_TOP);
		this.setBorder(border);

	}

	public void createReplay() {
		JButton replayButton = new JButton("Replay");
		replayButton.setActionCommand("replay");
		replayButton.addActionListener(driver);
		replayButton.setVisible(true);
		replayButton.setAlignmentX(CENTER_ALIGNMENT);
		replayButton.setAlignmentY(CENTER_ALIGNMENT);

		this.add(Box.createRigidArea(new Dimension(30, 30)));
		this.add(replayButton);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}

	public void createUndo() {
		JButton undoButton = new JButton("Undo");
		undoButton.setActionCommand("undo");
		undoButton.addActionListener(driver);
		undoButton.setVisible(true);
		undoButton.setAlignmentX(CENTER_ALIGNMENT);
		undoButton.setAlignmentY(CENTER_ALIGNMENT);

		this.add(Box.createRigidArea(new Dimension(5, 5)));
		this.add(undoButton);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}

	public void createBackground() {
		JButton backgroundButton = new JButton("Background");
		backgroundButton.setActionCommand("background");
		backgroundButton.addActionListener(driver);
		backgroundButton.setVisible(true);
		backgroundButton.setAlignmentX(CENTER_ALIGNMENT);
		backgroundButton.setAlignmentY(CENTER_ALIGNMENT);

		this.add(Box.createRigidArea(new Dimension(5, 5)));
		this.add(backgroundButton);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}

	public void createStart() {
		JButton startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(driver);
		startButton.setVisible(true);
		startButton.setAlignmentX(CENTER_ALIGNMENT);
		startButton.setAlignmentY(CENTER_ALIGNMENT);

		this.add(Box.createRigidArea(new Dimension(5, 5)));
		this.add(startButton);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}

	public void createPause() {
		JButton startButton = new JButton("Pause");
		startButton.setActionCommand("pause");
		startButton.addActionListener(driver);
		startButton.setVisible(true);
		startButton.setAlignmentX(CENTER_ALIGNMENT);
		startButton.setAlignmentY(CENTER_ALIGNMENT);

		this.add(Box.createRigidArea(new Dimension(5, 5)));
		this.add(startButton);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}

	public void createSave() {
		JButton saveButton = new JButton("Save");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(driver);
		saveButton.setVisible(true);
		this.add(saveButton);
	}

	public void createLoad() {
		JButton loadButton = new JButton("Load");
		loadButton.setActionCommand("load");
		loadButton.addActionListener(driver);
		loadButton.setVisible(true);
		this.add(loadButton);
	}

	public void createLayout() {
		JButton layoutButton = new JButton("Layout");
		layoutButton.setActionCommand("layout");
		layoutButton.addActionListener(driver);
		layoutButton.setVisible(true);
		layoutButton.setAlignmentX(CENTER_ALIGNMENT);
		layoutButton.setAlignmentY(CENTER_ALIGNMENT);

		this.add(Box.createRigidArea(new Dimension(5, 5)));
		this.add(layoutButton);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}

	public JComponent getControlElement() {
		return tendToAddButton;
	}

	public void addComponent(Element e) {
		elements.add(e);
	}

	@Override
	public void removeComponent(Element e) {
		elements.remove(e);
	}

	public PreviewPanel getPreview() {
		return this.preview;
	}

	public void setController(DesignController controller) {
		this.designController = controller;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Element element : elements) {
			element.draw(g);
		}
	}

	@Override
	public void draw(Graphics g) {
		// repaint();
	}

	@Override
	public void reset() {
		for (Element element : elements) {
			element.reset();
		}
	}

	@Override
	public void save(ObjectOutputStream op) {
	}

	@Override
	public Element load(ObjectInputStream ip) {
		return null;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		documentExeute(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		documentExeute(e);
	}

	public void documentExeute(DocumentEvent e) {
		JComponent owner = (JComponent) e.getDocument().getProperty("owner");
		Document d = e.getDocument();
		String content = "";
		try {
			content = d.getText(0, d.getLength());
		} catch (BadLocationException badLocationException) {
			log.error(badLocationException.getMessage());
		}
		if (owner.getName().equals("buttonNameField")) {
			tendToAddButton.setText(content);
		}
		if (owner.getName().equals("buttonHeightField")) {
			int height = 0;
			try {
				// checking valid integer using parseInt() method
				if (content.equals("")) {
					height = 0;
				} else {
					height = Integer.parseInt(content);
				}
			} catch (NumberFormatException notNumE) {
				log.error(notNumE.getMessage());
			}

			tendToAddButton.setPreferredSize(new Dimension(
					(int) tendToAddButton.getPreferredSize().getWidth(),
					height));
			tendToAddButton.revalidate();
		}
		if (owner.getName().equals("buttonWidthField")) {
			int width = 0;
			try {
				// checking valid integer using parseInt() method
				if (content.equals("")) {
					width = 0;
				} else {
					width = Integer.parseInt(content);
				}
			} catch (NumberFormatException notNumE) {
				log.error(notNumE.getMessage());
			}
			tendToAddButton.setPreferredSize(new Dimension(width,
					(int) tendToAddButton.getPreferredSize().getHeight()));
			tendToAddButton.revalidate();
		}
		if (owner.getName().equals("timerField")) {
			int time = 0;
			try {
				// checking valid integer using parseInt() method
				if (content.equals("")) {
					time = 0;
				} else {
					time = Integer.parseInt(content);
				}
			} catch (NumberFormatException notNumE) {
				log.error(notNumE.getMessage());
			}
		}

		if (owner.getName().equals("scoreField")) {
			int score = 0;
			try {
				// checking valid integer using parseInt() method
				if (content.equals("")) {
					score = 0;
				} else {
					score = Integer.parseInt(content);
				}
			} catch (NumberFormatException notNumE) {
				log.error(notNumE.getMessage());
			}
		}
		this.validate();
	}

	public void refresh(JComponent j) {
		j.revalidate();
		j.repaint();

	}

	public ArrayList<Collider> getColliders() {
		return colliders;
	}


	public void setColliders(ArrayList<Collider> colliders) {
		this.colliders = colliders;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public ArrayList<Command> getEventList() {
		return eventList;
	}

	public void setEventList(ArrayList<Command> eventList) {
		this.eventList = eventList;
	}

	public boolean isPushedElement() {
		return pushedElement;
	}

	public void setPushedElement(boolean pushedElement) {
		this.pushedElement = pushedElement;
	}
}
