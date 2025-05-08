package manager;

import static conf.GameConfig.BIRD_HEIGHT;
import static conf.GameConfig.DINO_HEIGHT;
import static conf.GameConfig.WINDOW_HEIGHT;
import static conf.GameConfig.WINDOW_WIDTH;

import java.awt.Graphics;
import java.util.ArrayList;

import entities.Bird;
import entities.Player;

public class BirdManager {
	private ArrayList<Bird> birds = new ArrayList<>();
	private int spawnTimer = 0;

	public void update(Player player, GameOverManager gameOverManager, long elapsedTime, boolean cactusOnScreen) {
		spawnTimer++;
		if (elapsedTime > 5000 && spawnTimer > 200 && !cactusOnScreen) {
			if (Math.random() < 0.4) {
				int height = (Math.random() > 0.5) ? WINDOW_HEIGHT - DINO_HEIGHT - BIRD_HEIGHT + 10
						: WINDOW_HEIGHT - BIRD_HEIGHT;
				birds.add(new Bird(WINDOW_WIDTH - 50, height));
			}
			spawnTimer = 0;
		}

		for (int i = 0; i < birds.size(); i++) {
			Bird b = birds.get(i);
			if (b.getBounds().intersects(player.getBounds())) {
				player.setDead(true);
				gameOverManager.setGameOver(true);
				break;
			}
			b.update();
			if (b.isOffScreen()) {
				birds.remove(i);
				i--;
			}
		}
	}

	public void render(Graphics g) {
		for (Bird b : birds) {
			b.render(g);
		}
	}

	public void reset() {
		birds.clear();
		spawnTimer = 0;
	}

	public boolean isEmpty() {
		return birds.isEmpty();
	}
}
