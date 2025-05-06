package main;

import static ultiz.Constants.Direction.DOWN;
import static ultiz.Constants.Direction.UP;
import static ultiz.Constants.PlayerConstants.DUCK;
import static ultiz.Constants.PlayerConstants.GetDino;
import static ultiz.Constants.PlayerConstants.JUMP;
import static ultiz.Constants.PlayerConstants.RUNNING;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel {
	private MouseInputs mouseInputs;

	private String[] frameNames = { "dino-run1.png", "dino-run2.png", "dino-jump.png", "dino-duck1.png",
			"dino-duck2.png", "dino-dead.png" };

	private int playAction = RUNNING;
	private int playDir = -1;
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 30;
	private int windowWidth = 750;
	private int windowHeight = 250;
	private int dinoHeight = 94, dinoWidth = 88;
	private int dinosaurX = 50, dinosourY = windowHeight - dinoHeight;
	private int yHeight = 17;

	public GamePanel() {

		mouseInputs = new MouseInputs(this);

		loadAnimation();

		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		setPanelSize();
	}

	private void loadAnimation() {
		animations = new BufferedImage[4][2];
		animations[0] = new BufferedImage[2]; // RUNNING
		animations[1] = new BufferedImage[1]; // JUMPING
		animations[2] = new BufferedImage[2]; // DUCKING
		animations[3] = new BufferedImage[1]; // DEAD

		try {
			animations[0][0] = ImageIO.read(getClass().getResourceAsStream("/dino-run1.png"));
			animations[0][1] = ImageIO.read(getClass().getResourceAsStream("/dino-run2.png"));
			animations[1][0] = ImageIO.read(getClass().getResourceAsStream("/dino-jump.png"));
			animations[2][0] = ImageIO.read(getClass().getResourceAsStream("/dino-duck1.png"));
			animations[2][1] = ImageIO.read(getClass().getResourceAsStream("/dino-duck2.png"));
			animations[3][0] = ImageIO.read(getClass().getResourceAsStream("/dino-dead.png"));
		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	private void setPanelSize() {
		Dimension size = new Dimension(windowWidth, windowHeight);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);

	}

	public void setDirection(int direction) {
		this.playDir = direction;
		if (direction == UP) {
			playAction = JUMP;
		} else if (direction == DOWN) {
			playAction = DUCK;
		}
	}

	public void updateGame() {
		updateAnimation();
		updatePos();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(animations[playAction][aniIndex], dinosaurX, dinosourY, dinoWidth, dinoHeight, null);

	}

	private void updatePos() {
		switch (playDir) {
		case UP:
			dinosourY -= yHeight;
			break;
		case DOWN:
			dinosourY += yHeight;
			break;
		}
		playDir = -1;

	}

	private void updateAnimation() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetDino(playAction)) {
				aniIndex = 0;
			}
		}

	}

}
