package conf;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class GameConfig {
	// Configurable fields
	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;
	public static String GAME_TITLE;

	public static int DINO_START_X;
	public static int DINO_WIDTH;
	public static int DINO_HEIGHT;
	public static float GRAVITY;
	public static float JUMP_SPEED;
	public static float DUCK_HEIGHT;

	public static int FPS_SET;
	public static int UPS_SET;

	public static int CACTUS_HEIGHT;
	public static long CACTUS_SPAWN_INTERVAL;

	public static int BIRD_WIDTH;
	public static int BIRD_HEIGHT;

	public static int FRONT_SIZE;
	public static float SPEED_ENTITIES;

	public static void loadConfig(String filename) {
		Properties props = new Properties();
		try {
			FileInputStream in = new FileInputStream(filename);
			props.load(in);

			GAME_TITLE = props.getProperty("game_title");
			WINDOW_WIDTH = Integer.parseInt(props.getProperty("window_width"));
			WINDOW_HEIGHT = Integer.parseInt(props.getProperty("window_height"));

			DINO_START_X = Integer.parseInt(props.getProperty("dino_start_x"));
			DINO_WIDTH = Integer.parseInt(props.getProperty("dino_width"));
			DINO_HEIGHT = Integer.parseInt(props.getProperty("dino_height"));
			GRAVITY = Float.parseFloat(props.getProperty("gravity"));
			JUMP_SPEED = Float.parseFloat(props.getProperty("jump_speed"));
			DUCK_HEIGHT = Float.parseFloat(props.getProperty("duck_height"));

			FPS_SET = Integer.parseInt(props.getProperty("fps"));
			UPS_SET = Integer.parseInt(props.getProperty("ups"));

			CACTUS_HEIGHT = Integer.parseInt(props.getProperty("cactus_height"));
			CACTUS_SPAWN_INTERVAL = Long.parseLong(props.getProperty("cactus_spawn_interval"));

			BIRD_WIDTH = Integer.parseInt(props.getProperty("bird_width"));
			BIRD_HEIGHT = Integer.parseInt(props.getProperty("bird_height"));

			FRONT_SIZE = Integer.parseInt(props.getProperty("font_size"));
			SPEED_ENTITIES = Float.parseFloat(props.getProperty("speed_entities"));

			in.close();
		} catch (IOException e) {
			System.out.println("Không tìm thấy config, tạo file mới mặc định.");
			createDefaultConfig(filename);
			loadConfig(filename);
		}
	}

	private static void createDefaultConfig(String filename) {
		String defaultContent = """
				game_title=Dino Runner
				window_width=750
				window_height=250

				dino_start_x=50
				dino_width=88
				dino_height=94
				gravity=0.05
				jump_speed=4.0
				duck_height=30.0

				fps=120
				ups=200

				cactus_height=70
				cactus_spawn_interval=2000

				bird_width=50
				bird_height=40

				font_size=20
				speed_entities=2.0
				""";

		try (FileWriter writer = new FileWriter(filename)) {
			writer.write(defaultContent);
		} catch (IOException e) {
			System.out.println("Không thể tạo file cấu hình.");
			e.printStackTrace();
		}
	}
}
