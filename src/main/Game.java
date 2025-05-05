package main;

public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_set = 120;
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

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_set;
		long lastFrame = System.nanoTime();
		long now = System.nanoTime();
		int frames = 0;
		long lastCheck = System.currentTimeMillis();
		while (true) {
			now = System.nanoTime();

			if (System.nanoTime() - lastFrame >= timePerFrame) {
				gamePanel.repaint();
				lastFrame = now;
				frames++;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS:" + frames);
				frames = 0;
			}
		}

	}
}
