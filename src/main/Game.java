package main;

public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_set = 120;
	private final int UPS_set = 200;
	public final static float Scale = 1.5f;

	public Game() {
		gamePanel = new GamePanel();
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		gameLoopStart();

	}

	private void gameLoopStart() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		gamePanel.updateGame();
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_set;
		double timePerUpdate = 1000000000.0 / UPS_set;

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
}
