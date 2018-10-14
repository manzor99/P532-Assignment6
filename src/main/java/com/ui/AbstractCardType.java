package com.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.gamemaker.GameMaker;
import com.infrastructure.ActionType;
import com.infrastructure.EventAction;
import com.infrastructure.EventType;

@SuppressWarnings("serial")
public abstract class AbstractCardType extends JPanel implements ItemListener {
	protected static Logger log = Logger.getLogger(GameMaker.class);
	public List<EventAction> getEventMap() {
		return eventMap;
	}

	private DesignPanel designPanel;
	private JFrame frame;
	private JCheckBox multipleQuantityCheckBox;
	private JTextField multipleQuantityTextField;
	private JTextField spriteName;
	private String selectedShape;
	private JTextField velX;
	private JTextField velY;
	private Color newColor;
	private BufferedImage bufferedImage = null;

	private List<EventAction> eventMap = new ArrayList<>();
	private JCheckBox chkScoreElement;
	private JCheckBox gameEnd;

	protected JTextField radius;
	protected JTextField width;
	protected JTextField height;
	protected JLabel widthLabel;
	protected JLabel heightLabel;
	protected JLabel radiusLabel;

	public AbstractCardType(DesignPanel designPanel, JFrame frame,
			String selectedShape) {
		this.designPanel = designPanel;
		this.frame = frame;
		this.setBorder(new EmptyBorder(15, 15, 15, 15));
		this.selectedShape = selectedShape;
		multipleQuantityCheckBox = new JCheckBox("Multiple Quantity:");
		multipleQuantityTextField = new JTextField();
		multipleQuantityTextField.setPreferredSize(new Dimension(100, 20));
		multipleQuantityTextField.setEnabled(false);
		createAbstractCard();
		newColor = Color.black;
	}

	public void createAbstractCard() {
		String nameLabel = selectedShape + designPanel.getElements().size();
		String radiusDefaultValue = "100";
		String widthDefaultValue = "100";
		String heightDefaultValue = "100";

		add(new JLabel("Object Name: ", JLabel.LEFT));
		spriteName = new JTextField(nameLabel, 20);
		add(spriteName);

		radiusLabel = new JLabel("Radius: ", JLabel.LEFT);
		add(radiusLabel);

		radius = new JTextField(radiusDefaultValue, 20);
		add(radius);

		widthLabel = new JLabel("Width: ", JLabel.LEFT);
		add(widthLabel);

		width = new JTextField(widthDefaultValue, 4);
		add(width);

		add(new JLabel("    "));

		heightLabel = new JLabel("Height: ", JLabel.LEFT);
		add(heightLabel);

		height = new JTextField(heightDefaultValue, 4);
		add(height);

		String xVelLabel = "0";
		String yVelLabel = "0";

		add(new JLabel("   X-Velocity: ", JLabel.LEFT));
		velX = new JTextField(xVelLabel, 4);
		add(velX);

		add(new JLabel("Y-Velocity: ", JLabel.LEFT));
		velY = new JTextField(yVelLabel, 4);
		add(velY);

		JButton colorButton = new JButton("Choose Color");
		colorButton.setAlignmentY(BOTTOM_ALIGNMENT);
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newColor = JColorChooser.showDialog(frame, "Choose Color",
						frame.getBackground());
			}
		});
		add(colorButton);

		JButton imageButton = new JButton("Choose Image");
		imageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					String sname = file.getAbsolutePath(); // THIS WAS THE
					try {
						bufferedImage = ImageIO.read(new File(sname));
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			}
		});
		add(imageButton);

		chkScoreElement = new JCheckBox("Score Element");
		add(chkScoreElement);

		gameEnd = new JCheckBox("Game End Element");
		add(gameEnd);

		add(multipleQuantityCheckBox);
		add(multipleQuantityTextField);
		multipleQuantityCheckBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					multipleQuantityTextField.setEnabled(true);
				} else {
					multipleQuantityTextField.setEnabled(false);
				}
			}
		});

		JButton eventAction = new JButton("Select Event-Action");
		eventAction.addActionListener(al -> {
			JTextField interval = new JTextField();
			JComboBox<EventType> eventBox = new JComboBox<>(EventType.values());
			JComboBox<ActionType> actionBox = new JComboBox<>(
					ActionType.values());
			actionBox.setVisible(true);
			Object[] message = { "Events:", eventBox, "Actions:", actionBox,
					"Interval:", interval };

			UIManager.put("OptionPane.yesButtonText", "Save");
			UIManager.put("OptionPane.noButtonText", "More Event-Action");
			UIManager.put("OptionPane.cancelButtonText", "Cancel");
			int option = JOptionPane.showConfirmDialog(null, message,
					"Sprite Details", JOptionPane.YES_NO_CANCEL_OPTION);
			if (option == JOptionPane.YES_OPTION
					|| option == JOptionPane.NO_OPTION) {
				eventMap.add(
						new EventAction((EventType) eventBox.getSelectedItem(),
								(ActionType) actionBox.getSelectedItem(),
								Integer.parseInt(interval.getText())));
			}
			if (option == JOptionPane.NO_OPTION) {
				Object[] eventMessage = { "Events:", eventBox, "Actions:",
						actionBox, "Interval:", interval };
				while (option == JOptionPane.NO_OPTION) {
					option = JOptionPane.showConfirmDialog(null, eventMessage,
							"Add event-action Details",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (option == JOptionPane.CANCEL_OPTION) {
						break;
					}
					eventMap.add(new EventAction(
							(EventType) eventBox.getSelectedItem(),
							(ActionType) actionBox.getSelectedItem(),
							Integer.parseInt(interval.getText())));
				}
			}
		});
		add(eventAction);
	}

	public SpriteProperties getSpriteProperties() {
		return new SpriteProperties(Integer.parseInt(velX.getText()),
				Integer.parseInt(velY.getText()), spriteName.getText(),
				bufferedImage, newColor, Integer.parseInt(width.getText()),
				Integer.parseInt(height.getText()), selectedShape,
				Integer.parseInt(radius.getText()),
				Integer.parseInt(multipleQuantityTextField.getText()), eventMap,
				chkScoreElement.isSelected(), gameEnd.isSelected());
	}
}
