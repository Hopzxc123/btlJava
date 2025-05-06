package entities;

import static ultiz.Constants.PlayerConstants.DUCK;
import static ultiz.Constants.PlayerConstants.GetDino;
import static ultiz.Constants.PlayerConstants.JUMP;
import static ultiz.Constants.PlayerConstants.RUNNING;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Entity {
	private String[] frameNames = { "dino-run1.png", "dino-run2.png", "dino-jump.png", "dino-duck1.png",
			"dino-duck2.png", "dino-dead.png" };

	private int playAction = RUNNING;
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 30;
	private int dinoHeight = 94, dinoWidth = 88;
	private int windowHeight = 250;
	private int dinosaurX = 50, dinosourY = windowHeight - dinoHeight;
	private boolean up, down;
	private boolean action = false;
	// jumping
	private boolean inAir = false;
	private float airSpeed = 0f;
	private float gravity = 0.6f;
	private float jumpSpeed = 12.0f;

	public Player(float x, float y) {
		super(x, y);
		loadAnimation();

	}

	public void update() {
		updatePos();
		setAnimation();
		updateAnimation();

	}

	public void render(Graphics g) {
		g.drawImage(animations[playAction][aniIndex], (int) x, (int) y, dinoWidth, dinoHeight, null);

	}

	private void updatePos() {
		action = false;
		if (inAir) {
			y += airSpeed;
			airSpeed += gravity;

			if (y >= dinosourY) {
				y = dinosourY;
				inAir = false;
				airSpeed = 0;
			}
		}

		action = true;
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

	public void setAnimation() {
		int previousAction = playAction;

		if (up && !down) {
			playAction = JUMP;
		} else if (!up && down) {
			playAction = DUCK;
		} else {
			playAction = RUNNING;
		}

		if (previousAction != playAction) {
			aniIndex = 0;
			aniTick = 0;
		}
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

	public void setBooleanDir() {
		up = false;
		down = false;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
		if (up && !this.inAir && y >= dinosourY) {
			this.inAir = true;
			this.airSpeed = -jumpSpeed; // reset lực nhảy
		}

	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

}
