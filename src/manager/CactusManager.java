package manager;

import static conf.GameConfig.CACTUS_HEIGHT;
import static conf.GameConfig.CACTUS_SPAWN_INTERVAL;
import static conf.GameConfig.WINDOW_HEIGHT;
import static conf.GameConfig.WINDOW_WIDTH;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import entities.Cactus;
import entities.Player;
import state.GameOverManager;

public class CactusManager {
	private ArrayList<Cactus> cactuses = new ArrayList<>();
	private long lastSpawnTime;
	private boolean canSpawn;
	private long spawnCooldown;

	public CactusManager() {
		lastSpawnTime = System.currentTimeMillis();
		canSpawn = true;
		spawnCooldown = 0;
	}

	public void update(Player player, GameOverManager gameOverManager) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastSpawnTime >= CACTUS_SPAWN_INTERVAL && canSpawn) {
			cactuses.add(new Cactus(WINDOW_WIDTH - 50, WINDOW_HEIGHT - CACTUS_HEIGHT));
			lastSpawnTime = currentTime;
			canSpawn = false;
		}

		Iterator<Cactus> iterator = cactuses.iterator();
		while (iterator.hasNext()) {
			Cactus c = iterator.next();
			if (c.getBounds().intersects(player.getBounds())) {
				player.setDead(true);
				gameOverManager.setGameOver(true);
				break;
			}
			c.update();
			if (c.isOffScreen()) {
				iterator.remove();
			}
		}

		if (cactuses.isEmpty() && !canSpawn) {
			if (spawnCooldown == 0) {
				spawnCooldown = currentTime + (long) (Math.random() * 2000 + 1000);
			}
			if (currentTime >= spawnCooldown) {
				canSpawn = true;
				spawnCooldown = 0;
			}
		}
	}

	public void render(Graphics g) {
		for (Cactus c : cactuses) {
			c.render(g);
		}
	}

	public void reset() {
		cactuses.clear();
		lastSpawnTime = System.currentTimeMillis();
		spawnCooldown = 0;
		canSpawn = true;
	}

	public boolean isEmpty() {
		return cactuses.isEmpty();
	}
}
