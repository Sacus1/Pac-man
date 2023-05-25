import java.util.Random;

public abstract class Ghost extends Thread{
	static int spawnX;
	static int spawnY;
	int positionX;
	int positionY;
	boolean isWeak = false;
	int timeWeak = 0;
	abstract void Move();
	void Reset() {
		positionX = spawnX;
		positionY = spawnY;
		isWeak = false;
	}
	protected void MoveRandomly(Model model){
		Random random = new Random();
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
		}
	}
	@Override
	public void run() {
		super.run();
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Move();
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
