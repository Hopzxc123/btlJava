package main;

import static conf.GameConfig.BIRD_HEIGHT;
import static conf.GameConfig.CACTUS_HEIGHT;
import static conf.GameConfig.CACTUS_SPAWN_INTERVAL;
import static conf.GameConfig.DINO_HEIGHT;
import static conf.GameConfig.DINO_START_X;
import static conf.GameConfig.FPS_SET;
import static conf.GameConfig.UPS_SET;
import static conf.GameConfig.WINDOW_HEIGHT;
import static conf.GameConfig.WINDOW_WIDTH;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import entities.Bird;
import entities.Cactus;
import entities.Player;

public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private Player player;
	private ArrayList<Cactus> cactuses;
	private long cactusSpawnCooldown; // Thời gian chờ trước khi spawn cactus mới
	private boolean canSpawnCactus; // Kiểm soát việc spawn cactus
	private long lastCactusSpawnTime;
	private boolean gameOver = false;
	ArrayList<Bird> birds = new ArrayList<>();
	int birdSpawnTimer = 0;
	private long gameStartTime;
	// Biến cho track
	private BufferedImage trackImage;
	private float trackX = 0;
	private float trackSpeed = 2.0f; // Tốc độ di chuyển của track
	private int trackHeight = 50; // Chiều cao của track
	// Biến cho background
	private BufferedImage backgroundImage;
	private float backgroundX = 0;
	private float backgroundSpeed = 0.5f;
	// gameOver
	private BufferedImage gameOverImage;
	private BufferedImage restartButtonImage; // Hình ảnh cho nút Restart
	private Rectangle restartButtonRect; // Vùng kích hoạt cho nút Restart
	private int restartButtonX, restartButtonY; // Vị trí của nút Restart
	// Quản lý điểm số
	private ScoreManager scoreManager;

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
		canSpawnCactus = true; // Ban đầu cho phép spawn cactus
		cactusSpawnCooldown = 0; // Khởi tạo thời gian chờ
		gameStartTime = System.currentTimeMillis();
		scoreManager = new ScoreManager();
		try {
			trackImage = ImageIO.read(getClass().getResourceAsStream("/track.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			gameOverImage = ImageIO.read(getClass().getResourceAsStream("/game-over.png")); // Thay đường dẫn tới file

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			restartButtonImage = ImageIO.read(getClass().getResourceAsStream("/reset.png"));
			// Tính vị trí và vùng kích hoạt cho nút Restart
			int buttonWidth = restartButtonImage.getWidth();
			int buttonHeight = restartButtonImage.getHeight();
			restartButtonX = WINDOW_WIDTH / 2 - buttonWidth / 2; // Căn giữa
			restartButtonY = WINDOW_HEIGHT / 2 + 20; // Dịch xuống dưới chữ "GAME OVER"
			restartButtonRect = new Rectangle(restartButtonX, restartButtonY, buttonWidth, buttonHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		long elapsedTime = currentTime - gameStartTime;
		if (currentTime - lastCactusSpawnTime >= CACTUS_SPAWN_INTERVAL && birds.isEmpty()) {
			cactuses.add(new Cactus(WINDOW_WIDTH - 50, WINDOW_HEIGHT - CACTUS_HEIGHT));
			lastCactusSpawnTime = currentTime;
			canSpawnCactus = false;
		}

		// Cập nhật vị trí cactus
		Iterator<Cactus> iterator = cactuses.iterator();
		while (iterator.hasNext()) {
			Cactus c = iterator.next();
			if (c.getBounds().intersects(player.getBounds())) {
				player.setDead(true);
				gameOver = true;
				scoreManager.setGameOver(true);
				break;

			}
			c.update();

			// Nếu cactus đã ra khỏi màn hình thì xoá
			if (c.isOffScreen()) {
				iterator.remove();
			}
		}
		boolean cactusOnScreen = !cactuses.isEmpty();
		if (!cactusOnScreen && !canSpawnCactus) {
			if (cactusSpawnCooldown == 0) {
				// Tạo thời gian chờ ngẫu nhiên từ 1000ms đến 3000ms
				cactusSpawnCooldown = currentTime + (long) (Math.random() * 2000 + 1000);
			}
			if (currentTime >= cactusSpawnCooldown) {
				canSpawnCactus = true; // Cho phép spawn cactus sau khi hết thời gian chờ
				cactusSpawnCooldown = 0; // Reset thời gian chờ
			}
		}
		birdSpawnTimer++;
		if (elapsedTime > 5000 && birdSpawnTimer > 200 && !cactusOnScreen) {
			if (Math.random() < 0.4) { // 40% cơ hội
				int height = (Math.random() > 0.5) ? WINDOW_HEIGHT - DINO_HEIGHT - BIRD_HEIGHT + 10
						: WINDOW_HEIGHT - BIRD_HEIGHT;
				birds.add(new Bird(WINDOW_WIDTH - 50, height));
			}
			birdSpawnTimer = 0;
		}
		for (Bird b : birds) {
			if (b.getBounds().intersects(player.getBounds())) {
				player.setDead(true);
				gameOver = true;
				scoreManager.setGameOver(true);
				break;
			}
		}
		for (int i = 0; i < birds.size(); i++) {
			Bird b = birds.get(i);
			b.update();
			if (b.isOffScreen()) {
				birds.remove(i);
				i--;
			}
		}
		// Cập nhật điểm số
		scoreManager.update();
		// Cập nhật vị trí track
		trackX -= trackSpeed;
		if (trackX <= -WINDOW_WIDTH) {
			trackX = 0; // Lặp lại track khi ra khỏi màn hình
		}
		backgroundX -= backgroundSpeed;
		if (backgroundX <= -WINDOW_WIDTH) {
			backgroundX = 0;
		}

	}

	public void render(Graphics g) {
		g.drawImage(backgroundImage, (int) backgroundX, 0, WINDOW_WIDTH, WINDOW_HEIGHT - trackHeight + 20, null);
		if (backgroundX < 0) {
			g.drawImage(backgroundImage, (int) backgroundX + WINDOW_WIDTH, 0, WINDOW_WIDTH,
					WINDOW_HEIGHT - trackHeight + 20, null);
		}
		int trackY = WINDOW_HEIGHT - trackHeight; // Đặt track ngay dưới chân khủng long
		g.drawImage(trackImage, (int) trackX, trackY, WINDOW_WIDTH, trackHeight, null);
		// Vẽ thêm một bản sao của track để tạo hiệu ứng liền mạch
		if (trackX < 0) {
			g.drawImage(trackImage, (int) trackX + WINDOW_WIDTH, trackY, WINDOW_WIDTH, trackHeight, null);
		}
		player.render(g);

		for (Cactus c : cactuses) {
			c.render(g);
		}

		for (Bird b : birds) {
			b.render(g);
		}
		// Hiển thị điểm số
		scoreManager.render(g);
		// Hiển thị Game Over và nút Restart khi gameOver = true
		if (gameOver) {
			// Vẽ hình ảnh "Game Over"
			int gameOverWidth = gameOverImage.getWidth();
			int gameOverHeight = gameOverImage.getHeight();
			int gameOverX = WINDOW_WIDTH / 2 - gameOverWidth / 2;
			int gameOverY = WINDOW_HEIGHT / 2 - gameOverHeight / 2 - 30; // Dịch lên trên để chừa chỗ cho nút
			g.drawImage(gameOverImage, gameOverX, gameOverY, gameOverWidth, gameOverHeight, null);

			// Vẽ nút Restart
			g.drawImage(restartButtonImage, restartButtonX, restartButtonY, restartButtonImage.getWidth(),
					restartButtonImage.getHeight(), null);

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

				frames = 0;
				updates = 0;
			}
		}

	}

	public void resetGame() {
		gameOver = false;
		player.setDead(false);
		player.setBooleanDir();
		player.setUp(false);
		player.setDown(false);
		cactuses.clear();
		birds.clear();
		trackX = 0;
		backgroundX = 0;
		lastCactusSpawnTime = System.currentTimeMillis();
		gameStartTime = System.currentTimeMillis();
		scoreManager.reset();
	}

	public void windowFocusLost() {
		player.setBooleanDir();
	}

	public Player getPlayer() {
		return player;
	}

	public boolean getGameOver() {
		return gameOver;
	}
}
