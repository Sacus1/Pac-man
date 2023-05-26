import java.util.Random;

public abstract class Ghost extends Thread{
	private static int spawnX;
	private static int spawnY;
	int positionX;
	int positionY;
	boolean isWeak = false;
	int timeWeak = 0;

	static int getSpawnX() {
		return spawnX;
	}

	static void setSpawnX(int spawnX) {
		Ghost.spawnX = spawnX;
	}

	static int getSpawnY() {
		return spawnY;
	}

	static void setSpawnY(int spawnY) {
		Ghost.spawnY = spawnY;
	}

	abstract void move();
	void reset() {
		positionX = spawnX;
		positionY = spawnY;
		isWeak = false;
	}
	protected Random random = new Random();
	protected void moveRandomly(Model model){
		int dir = random.nextInt(4);
		switch (dir) {
			case 0 -> {
				if (positionY > 0 && model.grid[positionX][positionY - 1] != 1) {
					positionY--;
				}
			}
			case 1 -> {
				if (positionX < 19 && model.grid[positionX + 1][positionY] != 1) {
					positionX++;
				}
			}
			case 2 -> {
				if (positionY < 19 && model.grid[positionX][positionY + 1] != 1) {
					positionY++;
				}
			}
			case 3 -> {
				if (positionX > 0 && model.grid[positionX - 1][positionY] != 1) {
					positionX--;
				}
			}
			default -> throw new IllegalStateException("Unexpected value: " + dir);
		}
	}
	// TODO change to use the frame time instead of the sleep time
	@Override
	public void run() {
		super.run();
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			move();
			if (isWeak) {
				timeWeak++;
				if (timeWeak == 10) {
					isWeak = false;
					timeWeak = 0;
				}
			}
			Model.getInstance().notifyObservers();
		}
	}
}
