package main;

import static conf.GameConfig.DINO_START_X;
import static conf.GameConfig.FPS_SET;
import static conf.GameConfig.SPEED_ENTITIES;
import static conf.GameConfig.UPS_SET;

import java.awt.Graphics;

import entities.Environment;
import entities.Player;
import manager.BirdManager;
import manager.CactusManager;
import manager.GameOverManager;
import manager.ScoreManager;

public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private Player player;
	private Environment environment;
	private long gameStartTime;
	private ScoreManager scoreManager;
	private GameOverManager gameOverManager;
	private CactusManager cactusManager;
	private BirdManager birdManager;
	private int lastSpeedUpScore = 0; // lưu điểm khi gần nhất đã tăng tốc

	public Game() {
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);

		gamePanel.requestFocus();
		gameLoopStart();

	}

	private void initClasses() {
		player = new Player((float) DINO_START_X, 0);
		gameStartTime = System.currentTimeMillis();
		scoreManager = new ScoreManager();
		environment = new Environment();
		gameOverManager = new GameOverManager();
		cactusManager = new CactusManager();
		birdManager = new BirdManager();

	}

	private void gameLoopStart() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {

		if (gameOverManager.isGameOver()) {
			return; // Không cập nhật gì nữa nếu đã game over
		}
		player.update();

		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - gameStartTime;
		boolean birdOnScreen = !birdManager.isEmpty();
		boolean cactusOnScreen = !cactusManager.isEmpty();

		cactusManager.update(player, scoreManager, gameOverManager, birdOnScreen);
		birdManager.update(player, scoreManager, gameOverManager, elapsedTime, cactusOnScreen);

		// Cập nhật điểm số
		scoreManager.update();
		int currentScore = scoreManager.getScore();
		if (currentScore != 0 && currentScore % 100 == 0 && currentScore != lastSpeedUpScore) {
			SPEED_ENTITIES += 0.5;
			lastSpeedUpScore = currentScore;
		}
		// Cập nhật vị trí track
		environment.update();

	}

	public void render(Graphics g) {
		environment.render(g);
		player.render(g);
		cactusManager.render(g);
		birdManager.render(g);

		// Hiển thị điểm số
		scoreManager.render(g);
		// Hiển thị Game Over và nút Restart khi gameOver = true
		gameOverManager.render(g);
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;
		while (true) {

			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();

				frames = 0;
				updates = 0;
			}
		}

	}

	public void resetGame() {
		lastSpeedUpScore = 0;
		SPEED_ENTITIES = 2.0f;
		gameOverManager.reset();
		player.setDead(false);
		player.setBooleanDir();
		player.setUp(false);
		player.setDown(false);
		environment.reset();
		scoreManager.reset();
		cactusManager.reset();
		birdManager.reset();

	}

	public void windowFocusLost() {
		player.setBooleanDir();
	}

	public Player getPlayer() {
		return player;
	}

	public boolean getGameOver() {
		return gameOverManager.isGameOver();
	}
}
