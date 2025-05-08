package manager;

import static conf.GameConfig.WINDOW_HEIGHT;
import static conf.GameConfig.WINDOW_WIDTH;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameOverManager {
	private boolean gameOver = false;
	private BufferedImage gameOverImage;
	private BufferedImage restartButtonImage;
	private Rectangle restartButtonRect;
	private int restartButtonX, restartButtonY;

	public GameOverManager() {
		try {
			gameOverImage = ImageIO.read(getClass().getResourceAsStream("/game-over.png"));
			restartButtonImage = ImageIO.read(getClass().getResourceAsStream("/reset.png"));

			int buttonWidth = restartButtonImage.getWidth();
			int buttonHeight = restartButtonImage.getHeight();
			restartButtonX = WINDOW_WIDTH / 2 - buttonWidth / 2;
			restartButtonY = WINDOW_HEIGHT / 2 + 20;
			restartButtonRect = new Rectangle(restartButtonX, restartButtonY, buttonWidth, buttonHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		if (!gameOver)
			return;

		int gameOverWidth = gameOverImage.getWidth();
		int gameOverHeight = gameOverImage.getHeight();
		int gameOverX = WINDOW_WIDTH / 2 - gameOverWidth / 2;
		int gameOverY = WINDOW_HEIGHT / 2 - gameOverHeight / 2 - 30;

		g.drawImage(gameOverImage, gameOverX, gameOverY, gameOverWidth, gameOverHeight, null);
		g.drawImage(restartButtonImage, restartButtonX, restartButtonY, null);
	}

	public void setGameOver(boolean value) {
		this.gameOver = value;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public Rectangle getRestartButtonRect() {
		return restartButtonRect;
	}

	public void reset() {
		gameOver = false;
	}
}
