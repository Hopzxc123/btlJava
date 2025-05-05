package inputs;

import static ultiz.Constants.Direction.DOWN;
import static ultiz.Constants.Direction.UP;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_W:
		case KeyEvent.VK_PAGE_UP:
			gamePanel.setDirection(UP);
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_PAGE_DOWN:
			gamePanel.setDirection(DOWN);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
