package ultiz;

public class Constants {
	public static class Direction {
		public static final int UP = 0;
		public static final int DOWN = 1;
	}

	public static class PlayerConstants {
		public static final int RUNNING = 0;
		public static final int JUMP = 1;
		public static final int DUCK = 2;
		public static final int DEAD = 3;

		public static int GetDino(int play_action) {
			switch (play_action) {
			case RUNNING:
			case DUCK:
				return 2;
			default:
				return 1;
			}
		}

	}
}
