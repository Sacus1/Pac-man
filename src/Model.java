import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Model implements Observable {
	private static Model instance = null;
	private Controller controller;
	/**
	 * 0 : Nothing
	 * 1 : Wall
	 * 2 : Pacman
	 * 3 : Food
	 * 4 : BigFood
	 */
	 int[][] grid;
	 int pacmanPositionX;
	 int pacmanPositionY;
	 int pacmanSpawnX;
	 int pacmanSpawnY;
	 Ghost[] ghosts = new Ghost[4];

	 Direction nextMove = Direction.NONE;

	Direction orientation = Direction.RIGHT;
	private int score = 0;
	private boolean win = false;
	private int life = 3;
	boolean start = false;
	private Model() {
		File file = new File("map");
		grid = new int[17][17];
		// go through each line of the file
		try(FileReader fileReader = new FileReader(file)){
			createMap(fileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createMap(FileReader fileReader) throws IOException {
		int c;
		int y = 0;
		int x = 0;
		while ((c = fileReader.read()) != -1) {
			char character = (char) c;
			switch (character) {
				case '0' -> grid[x][y] = 3; // Food
				case '1' -> grid[x][y] = 1; // Wall
				case '2' -> grid[x][y] = 0; // Nothing
				case '3' -> grid[x][y] = 4; // BigFood
				case '4' -> {
					// pacman
					grid[x][y] = 2;
					pacmanSpawnX = x;
					pacmanSpawnY = y;
				}
				case '5' -> {
					// Ghost spawn
					grid[x][y] = 0;
					Ghost.setSpawnX(x);
					Ghost.setSpawnY(y);
				}
				case '\n' -> {
					y++;
					x = -1;
				}
				default -> throw new IllegalStateException("Unexpected value: " + character);
			}
			x++;
		}
	}

	public void start() {
		ghosts[0] = new Blinky();
		ghosts[0].start();
		ghosts[1] = new Pinky();
		ghosts[1].start();
		ghosts[2] = new Inky();
		ghosts[2].start();
		// TODO change to use the frame time instead of the sleep time
		new Thread(() -> {
			changePosition(pacmanSpawnX, pacmanSpawnY);
			while (!win) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ignored) {}
				move(nextMove);
			}
		}).start();
		start = true;
	}
	public static Model getInstance() {
		if (instance == null) {
			synchronized (Model.class) {
				if (instance == null) {
					instance = new Model();
				}
			}
		}
		return instance;
	}

	private void die() {
		life--;
		if (life == 0) {
			// Game over
			JOptionPane.showMessageDialog(null, "Game Over");
			System.exit(0);
		}
		// Reset the game
		changePosition(pacmanSpawnX, pacmanSpawnY);
		// Reset the ghosts
		for (Ghost ghost : ghosts) {
			if (ghost != null) {
				ghost.reset();
			}
		}
		notifyObservers();
	}

	private void changePosition(int x, int y) {
		grid[pacmanPositionX][pacmanPositionY] = 0;
		grid[x][y] = 2;
		pacmanPositionX = x;
		pacmanPositionY = y;
	}

	private void move(Direction dir) {
		switch (dir) {
			case UP -> moveUp();
			case LEFT -> moveLeft();
			case DOWN -> moveDown();
			case RIGHT -> moveRight();
			default -> throw new IllegalStateException("Unexpected value: " + dir);
		}
		ghostCollision();
		hasWon();
		notifyObservers();
	}

	private void moveRight() {
		// check if the next position is out of the map then move to the other side.
		if (pacmanPositionX + 1 > 16) {
			changePosition(0, pacmanPositionY);
		}
		else
		{
			// if there's a wall to the right of pacman then move continue in the same direction.
			if (orientation != Direction.RIGHT && (grid[pacmanPositionX + 1][pacmanPositionY] == 1)) {
				move(orientation);
				return;
			}
			// if there's no wall then move to the next position.
			if (grid[pacmanPositionX + 1][pacmanPositionY] != 1) {
				score(pacmanPositionX + 1, pacmanPositionY);
				changePosition(pacmanPositionX + 1, pacmanPositionY);
				orientation = Direction.RIGHT;
			}
		}
	}

	private void moveDown() {
		// check if the next position is out of the map then move to the other side.
		if (pacmanPositionY + 1 > 16) {
			changePosition(pacmanPositionX, 0);
		} else {
			// if there's a wall down to pacman then move continue in the same direction.
			if (orientation != Direction.DOWN && (grid[pacmanPositionX][pacmanPositionY + 1] == 1)) {
					move(orientation);
					return;

			}
			// if there's no wall then move to the next position.
			if (grid[pacmanPositionX][pacmanPositionY + 1] != 1) {
				score(pacmanPositionX, pacmanPositionY + 1);
				changePosition(pacmanPositionX, pacmanPositionY + 1);
				orientation = Direction.DOWN;
			}
		}
	}

	private void moveLeft() {
		// check if the next position is out of the map then move to the other side.
		if (pacmanPositionX - 1 < 0) {
			changePosition(16, pacmanPositionY);
		} else {
			// if there's a wall left to pacman then move continue in the same direction.
			if (orientation != Direction.LEFT && (grid[pacmanPositionX - 1][pacmanPositionY] == 1)) {
					move(orientation);
					return;
			}
			// if there's no wall then move to the next position.
			if (grid[pacmanPositionX - 1][pacmanPositionY] != 1) {
				score(pacmanPositionX - 1, pacmanPositionY);
				changePosition(pacmanPositionX - 1, pacmanPositionY);
				orientation = Direction.LEFT;
			}
		}
	}

	private void moveUp() {
		// check if the next position is out of the map then move to the other side.
		if (pacmanPositionY - 1 < 0) {
			changePosition(pacmanPositionX, 16);
		} else {
		// if there's a wall up to pacman then move continue in the same direction.
		if (orientation != Direction.UP && (grid[pacmanPositionX][pacmanPositionY - 1] == 1)) {
				move(orientation);
				return;
		}
			// if there's no wall then move to the next position.
			if (grid[pacmanPositionX][pacmanPositionY - 1] != 1) {
				score(pacmanPositionX, pacmanPositionY - 1);
				changePosition(pacmanPositionX, pacmanPositionY - 1);
				orientation = Direction.UP;
			}
		}
	}

	private void ghostCollision() {
		for (Ghost ghost : ghosts) {
			if (ghost == null) continue;
			if (ghost.positionX == pacmanPositionX && ghost.positionY == pacmanPositionY) {
				if (ghost.isWeak){
					ghost.reset();
					score += 100;
				} else {
					die();
				}
			}
		}
	}

	private void hasWon() {
		// Check if the player won
		win = true;
		for (int[] rows : grid) {
			for (int object : rows) {
				if (object == 3 || object == 4) {
					win = false;
					break;
				}
			}
		}
	}

	private void score(int x, int y) {
		if (grid[x][y] == 3)
			score += 10;
		if (grid[x][y] == 4) {
			score += 50;
			// weak ghosts
			for (Ghost ghost : ghosts) {
				if (ghost == null) {
					continue;
				}
				ghost.isWeak = true;
			}
		}
	}

	Controller getController() {
		return controller;
	}

	void setController(Controller controller) {
		this.controller = controller;
	}

	int getScore() {
		return score;
	}

	boolean isWin() {
		return win;
	}

	int getLife() {
		return life;
	}

}
