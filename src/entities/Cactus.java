package entities;

import static conf.GameConfig.SPEED_ENTITIES;
import static conf.GameConfig.WINDOW_HEIGHT;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Cactus extends Entity {
	private int width = 34, height = 70; // bạn có thể điều chỉnh

	private BufferedImage[] imageCactus;
	private BufferedImage currentImage;

	public Cactus(float x, float y) {
		super(x, y);
		loadImage();

	}

	private void loadImage() {
		imageCactus = new BufferedImage[3];
		int[] fixedWidths = { 34, 69, 102 };
		try {
			imageCactus[0] = ImageIO.read(getClass().getResourceAsStream("/big-cactus1.png"));
			imageCactus[1] = ImageIO.read(getClass().getResourceAsStream("/big-cactus2.png"));
			imageCactus[2] = ImageIO.read(getClass().getResourceAsStream("/big-cactus3.png"));

			double index = Math.random(); // chọn ảnh ngẫu nhiên
			if (index > .90) {
				currentImage = imageCactus[2];
				width = fixedWidths[2];
			} else if (index > .70) {
				currentImage = imageCactus[1];
				width = fixedWidths[1];
			} else {
				currentImage = imageCactus[0];
				width = fixedWidths[0];
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	public void update() {
		x -= SPEED_ENTITIES;
	}

	public void render(Graphics g) {
		int drawY = WINDOW_HEIGHT - height;

		g.drawImage(currentImage, (int) x, drawY, width, height, null);
	}

	public boolean isOffScreen() {
		return x + width < 0;
	}

}
