package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.Game;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {
	private GamePanel gamePanel;

	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Game game = gamePanel.getGame();
		if (game.getGameOver()) {
			game.resetGame();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Game game = gamePanel.getGame();
		if (game.getGameOver()) {
			game.resetGame();
		} else if (game.isWaitingToStart()) {
			game.setWaitingToStart(false); // bắt đầu game khi nhấn phím
		} else {
			gamePanel.getGame().getPlayer().setUp(true);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		gamePanel.getGame().getPlayer().setUp(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
