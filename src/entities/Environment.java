package entities;

import static conf.GameConfig.WINDOW_HEIGHT;
import static conf.GameConfig.WINDOW_WIDTH;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Environment {
	private BufferedImage backgroundImage;
	private BufferedImage trackImage;

	private float backgroundX = 0;
	private float trackX = 0;

	private final float backgroundSpeed = 0.5f;
	private final float trackSpeed = 2.0f;
	private final int trackHeight = 50;

	public Environment() {
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background.png"));
			trackImage = ImageIO.read(getClass().getResourceAsStream("/track.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		backgroundX -= backgroundSpeed;
		if (backgroundX <= -WINDOW_WIDTH)
			backgroundX = 0;

		trackX -= trackSpeed;
		if (trackX <= -WINDOW_WIDTH)
			trackX = 0;
	}

	public void render(Graphics g) {
		g.drawImage(backgroundImage, (int) backgroundX, 0, WINDOW_WIDTH, WINDOW_HEIGHT - trackHeight + 20, null);
		g.drawImage(backgroundImage, (int) backgroundX + WINDOW_WIDTH, 0, WINDOW_WIDTH,
				WINDOW_HEIGHT - trackHeight + 20, null);

		int trackY = WINDOW_HEIGHT - trackHeight;
		g.drawImage(trackImage, (int) trackX, trackY, WINDOW_WIDTH, trackHeight, null);
		g.drawImage(trackImage, (int) trackX + WINDOW_WIDTH, trackY, WINDOW_WIDTH, trackHeight, null);
	}

	public void reset() {
		backgroundX = 0;
		trackX = 0;
	}
}
