package manager;

import static conf.GameConfig.FRONT_SIZE;
import static conf.GameConfig.WINDOW_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class ScoreManager {
	private int score;
	private long lastScoreUpdateTime;
	private boolean gameOver;
	private Connection connection;

	public ScoreManager() {
		this.score = 0;
		this.lastScoreUpdateTime = System.currentTimeMillis();
		this.gameOver = false;
		initializeDatabase();
	}

	private void initializeDatabase() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:scores.db");
			Statement statement = connection.createStatement();
			String createTableSQL = "CREATE TABLE IF NOT EXISTS scores (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "Diem INTEGER NOT NULL," + "ThoiGianChoi  TIMESTAMP NOT NULL)";
			statement.execute(createTableSQL);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveScore() {
		if (!gameOver)
			return;

		try {
			String insertSQL = "INSERT INTO scores (Diem, ThoiGianChoi) VALUES (?, ?)";
			PreparedStatement pstmt = connection.prepareStatement(insertSQL);
			pstmt.setInt(1, score);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String formattedTime = sdf.format(new java.util.Date());
			pstmt.setString(2, formattedTime);

			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getHighScore() {
		int highScore = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT MAX(Diem) as high_score FROM scores");
			if (rs.next()) {
				highScore = rs.getInt("high_score");
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return highScore;
	}

	public void update() {
		if (gameOver) {
			return;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastScoreUpdateTime >= 100) {
			score += 1;
			lastScoreUpdateTime = currentTime;
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, FRONT_SIZE));

		// Chuỗi điểm số
		String scoreText = "Score: " + score;
		String highScoreText = "High Score: " + getHighScore();

		// Lấy kích thước chữ
		FontMetrics fm = g.getFontMetrics();

		// Tính độ rộng của từng chuỗi
		int scoreWidth = fm.stringWidth(scoreText);
		int highScoreWidth = fm.stringWidth(highScoreText);

		// Tính tổng chiều rộng và bắt đầu vẽ từ giữa
		int totalWidth = scoreWidth + highScoreWidth + 40; // khoảng cách giữa 2 chuỗi là 40 pixel
		int startX = (WINDOW_WIDTH - totalWidth) / 2;
		int y = 20;

		// Vẽ chuỗi
		g.drawString(scoreText, startX, y);
		g.drawString(highScoreText, startX + scoreWidth + 40, y);

	}

	public void reset() {
		score = 0;
		lastScoreUpdateTime = System.currentTimeMillis();
		gameOver = false;
	}

	public int getScore() {
		return score;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
		if (gameOver) {
			saveScore();
		}
	}

	public void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}