package main;

import static conf.GameConfig.CACTUS_HEIGHT;
import static conf.GameConfig.CACTUS_SPAWN_INTERVAL;
import static conf.GameConfig.CACTUS_SPAWN_X;
import static conf.GameConfig.DINO_START_X;
import static conf.GameConfig.FPS_SET;
import static conf.GameConfig.UPS_SET;
import static conf.GameConfig.WINDOW_HEIGHT;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import entities.Cactus;
import entities.Player;

public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private Player player;
	private ArrayList<Cactus> cactuses;
	private long lastCactusSpawnTime;
	boolean gameOver = false;

	public Game() {
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);

		gamePanel.requestFocus();
		gameLoopStart();

	}

	private void initClasses() {
		player = new Player((float) DINO_START_X, 0);
		cactuses = new ArrayList<>();
		lastCactusSpawnTime = System.currentTimeMillis();

	}

	private void gameLoopStart() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		if (gameOver) {
			return; // Không cập nhật gì nữa nếu đã game over
		}
		player.update();

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastCactusSpawnTime >= CACTUS_SPAWN_INTERVAL) {
			cactuses.add(new Cactus(CACTUS_SPAWN_X, WINDOW_HEIGHT - CACTUS_HEIGHT)); // spawn bên phải màn hình
			lastCactusSpawnTime = currentTime;
		}

		// Cập nhật vị trí cactus
		Iterator<Cactus> iterator = cactuses.iterator();
		while (iterator.hasNext()) {
			Cactus c = iterator.next();
			if (c.getBounds().intersects(player.getBounds())) {
				player.setDead(true);
				gameOver = true;

			}
			c.update();

			// Nếu cactus đã ra khỏi màn hình thì xoá
			if (c.isOffScreen()) {
				iterator.remove();
			}
		}

	}

	public void render(Graphics g) {
		player.render(g);

		for (Cactus c : cactuses) {
			c.render(g);
		}
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
				System.out.println("FPS:" + frames + "| UPS:" + updates);

				frames = 0;
				updates = 0;
			}
		}

	}

	public void windowFocusLost() {
		player.setBooleanDir();
	}

	public Player getPlayer() {
		return player;
	}
}
