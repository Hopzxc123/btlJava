package main;

import static conf.GameConfig.WINDOW_HEIGHT;
import static conf.GameConfig.WINDOW_WIDTH;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel {
	private MouseInputs mouseInputs;
	private Game game;

	public GamePanel(Game game) {

		mouseInputs = new MouseInputs(this);
		this.game = game;
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		setFocusable(true);
		setPanelSize();
	}

	private void setPanelSize() {
		Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);

	}

	public void updateGame() {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);

	}

	public Game getGame() {
		return game;
	}

}
