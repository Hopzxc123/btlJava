package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Game;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {
	private GamePanel gamePanel;

	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Game game = gamePanel.getGame();
		if (game.getGameOver()) {
			game.resetGame(); // Gọi phương thức reset từ Game
		} else {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_W:
				gamePanel.getGame().getPlayer().setUp(true);
				break;
			case KeyEvent.VK_S:
				gamePanel.getGame().getPlayer().setDown(true);
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_W:
			gamePanel.getGame().getPlayer().setUp(false);
			break;
		case KeyEvent.VK_S:
			gamePanel.getGame().getPlayer().setDown(false);
			break;
		}
	}

}
