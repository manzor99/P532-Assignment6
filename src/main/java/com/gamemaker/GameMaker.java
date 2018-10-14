package com.gamemaker;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//import com.component.Ball;
//import com.component.Brick;
//import com.component.Clock;
//import com.component.Paddle;
import com.controller.DesignController;
import com.controller.MainController;
import com.helper.CollisionChecker;
import com.timer.GameTimer;
import com.ui.ControlPanel;
import com.ui.DesignPanel;
import com.ui.GUI;
import com.ui.GamePanel;
import com.ui.MainPanel;

public class GameMaker {
	protected static Logger log = Logger.getLogger(GameMaker.class);

	public static void startGame(boolean isRestart) {

		/**
		 * We need: - Main panel: holds everything - Design panel: holds ui for creating
		 * objects - Game panel: area where graphic elements are placed - Control panel:
		 * area where control buttons are placed
		 */
		GameTimer observable = new GameTimer();

		log.info("Game Timer (Observable) created");
		// Create our 4 panels
		MainPanel mainPanel = new MainPanel();
		log.info("MainPanel created");
		DesignPanel designPanel = new DesignPanel();
		log.info("DesignPanel created");
		GamePanel gamePanel = new GamePanel();
		log.info("GamePanel created");
		ControlPanel controlPanel = new ControlPanel();
		log.info("ControlPanel created");

		// Add everything to mainPanel in order
		mainPanel.addComponent(designPanel);
		log.info("Added DesignPanel to MainPanel");
		mainPanel.addComponent(gamePanel);
		log.info("Added GamePanel to MainPanel");
		mainPanel.addComponent(controlPanel);
		log.info("Added ControlPanel to MainPanel");

		// Create the GUI class and pass all the panels
		GUI gui = new GUI(mainPanel, designPanel, gamePanel, controlPanel);
		log.info("GUI created");
		gui.addComponent(mainPanel);
		log.info("Added MainPanel to GUI");

		// ! Not sure where this logic will end up
		// CollisionChecker checker = new CollisionChecker();
		DesignController controller = new DesignController(gui);
		log.info("DesignController/GameMaker controller created");

		MainController driver = new MainController(gui, observable, controller, new CollisionChecker());
		log.info("MainController created as driver");
		controller.setMainController(driver);
		gamePanel.setDesignController(controller);
		gamePanel.addControllerListener(controller);
		log.info("Added DesignController as listener to GamePanel");
		designPanel.setController(controller);
		gui.addDriver(driver);
		observable.startTimer();
		log.info("Started Game Timer");
		gui.setVisible(true);
		gui.draw(null);
		gui.pack();
	}

	public static void runGame() {
		PropertyConfigurator.configure("log4j.properties");

		try {
			//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGame(false);
			}
		});
	}

	/*public static void main(String args[]) {

		 * PropertyConfigurator.configure("log4j.properties");
		 *
		 * try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		 * } catch (Exception e) { log.error(e.getMessage()); }
		 *
		 * SwingUtilities.invokeLater(new Runnable() {
		 *
		 * @Override public void run() { startGame(false); } });
		 *
		runGame();
	}*/

}
