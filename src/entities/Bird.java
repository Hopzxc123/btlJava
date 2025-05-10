package entities;

import static conf.GameConfig.SPEED_ENTITIES;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bird {
	private int x, y;
	private int width = 50, height = 40;

	private BufferedImage[] frames;
	private int aniTick = 0, aniIndex = 0, aniSpeed = 60;

	public Bird(int x, int y) {
		this.x = x;
		this.y = y;
		loadFrames();
	}

	private void loadFrames() {
		frames = new BufferedImage[2];
		try {
			frames[0] = ImageIO.read(getClass().getResourceAsStream("/bird1.png"));
			frames[1] = ImageIO.read(getClass().getResourceAsStream("/bird2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		x -= SPEED_ENTITIES;
		updateAnimation();
	}

	private void updateAnimation() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex = (aniIndex + 1) % frames.length;
		}
	}

	public void render(Graphics g) {
		g.drawImage(frames[aniIndex], x, y, width, height, null);
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	public boolean isOffScreen() {
		return x + width < 0;
	}
}
