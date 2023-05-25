import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;

public class Model extends Observable {
	private static Model instance = null;
	public Controller controller;
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

	 int nextMove = -1;
	/**
	 * 0 : Down
	 * 1 : Right
	 * 2 : Up
	 * 3 : Left
	 */
	short orientation = 1;
	public int score = 0;
	public boolean win = false;
	public int life = 3;
	boolean start = false;
	private Model() {
		File file = new File("map");
		grid = new int[17][17];
		// go through each line of the file
		try(FileReader fileReader = new FileReader(file)){
			int c;
			int Y = 0;
			int X = 0;
			while ((c = fileReader.read()) != -1) {
				char character = (char) c;
				switch (character) {
					case '0' -> grid[X][Y] = 3; // Food
					case '1' -> grid[X][Y] = 1; // Wall
					case '2' -> grid[X][Y] = 0; // Nothing
					case '3' -> grid[X][Y] = 4; // BigFood
					case '4' -> {
						// pacman
						grid[X][Y] = 2;
						pacmanSpawnX = X;
						pacmanSpawnY = Y;
					}
					case '5' -> {
						// Ghost spawn
						grid[X][Y] = 0;
						Ghost.spawnX = X;
						Ghost.spawnY = Y;
					}
				}
				X++;
				if (character == '\n') {
					Y++;
					X = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void Start() {
		ghosts[0] = new Blinky();
		ghosts[0].start();
		ghosts[1] = new Pinky();
		ghosts[1].start();
		ghosts[2] = new Inky();
		ghosts[2].start();
		new Thread(() -> {
			ChangePosition(pacmanSpawnX, pacmanSpawnY);
			while (!win) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ignored) {}
				Move(nextMove);
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

	private void Died() {
		life--;
		if (life == 0) {
			// Game over
			JOptionPane.showMessageDialog(null, "Game Over");
			System.exit(0);
		}
		// Reset the game
		ChangePosition(pacmanSpawnX, pacmanSpawnY);
		// Reset the ghosts
		for (Ghost ghost : ghosts) {
			if (ghost != null) {
				ghost.Reset();
			}
		}
		notifyObservers();
	}
	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	private void ChangePosition(int x, int y) {
		grid[pacmanPositionX][pacmanPositionY] = 0;
		grid[x][y] = 2;
		pacmanPositionX = x;
		pacmanPositionY = y;
	}

	private void Move(int dir) {
		switch (dir) {
			//Up
			case 0 -> {
				// check if the next position is out of the map then move to the other side
				if (pacmanPositionY - 1 < 0) {
					ChangePosition(pacmanPositionX, 16);
				} else {
				// if orientation is not up
				if (orientation != 2) {
					if (grid[pacmanPositionX][pacmanPositionY - 1] == 1) {
						// if there is a wall up to pacman then move to direction
						// convert orientation into direction
						switch (orientation) {
							case 0 -> Move(2);
							case 1 -> Move(3);
							case 3 -> Move(1);
						}
						break;
					}
				}
					// if there is no wall then move to the next position
					if (grid[pacmanPositionX][pacmanPositionY - 1] != 1) {
						Score(pacmanPositionX, pacmanPositionY - 1);
						ChangePosition(pacmanPositionX, pacmanPositionY - 1);
						orientation = 2;
					}
				}
			}
			// Left
			case 1 -> {
				// check if the next position is out of the map then move to the other side
				if (pacmanPositionX - 1 < 0) {
					ChangePosition(16, pacmanPositionY);
				} else {
				// if orientation is not left
				if (orientation != 3) {
					if (grid[pacmanPositionX - 1][pacmanPositionY] == 1) {
						// if there is a wall left to pacman then move to direction
						// convert orientation into direction
						switch (orientation) {
							case 0 -> Move(2);
							case 1 -> Move(3);
							case 2 -> Move(0);
						}
						break;
					}
				}
					// if there is no wall then move to the next position
					if (grid[pacmanPositionX - 1][pacmanPositionY] != 1) {
						Score(pacmanPositionX - 1, pacmanPositionY);
						ChangePosition(pacmanPositionX - 1, pacmanPositionY);
						orientation = 3;
					}
				}
			}
			// Down
			case 2 -> {
				// check if the next position is out of the map then move to the other side
				if (pacmanPositionY + 1 > 16) {
					ChangePosition(pacmanPositionX, 0);
				} else {
				// if orientation is not down
				if (orientation != 0) {
					if (grid[pacmanPositionX][pacmanPositionY + 1] == 1) {
						// if there is a wall down to pacman then move to direction
						// convert orientation into direction
						switch (orientation) {
							case 1 -> Move(3);
							case 2 -> Move(0);
							case 3 -> Move(1);
						}
						break;
					}
				}
					// if there is no wall then move to the next position
					if (grid[pacmanPositionX][pacmanPositionY + 1] != 1) {
						Score(pacmanPositionX, pacmanPositionY + 1);
						ChangePosition(pacmanPositionX, pacmanPositionY + 1);
						orientation = 0;
					}
				}
			}
			// Right
			case 3 -> {
				// check if the next position is out of the map then move to the other side
				if (pacmanPositionX + 1 > 16) {
					ChangePosition(0, pacmanPositionY);
				} else {
				// if orientation is not right
				if (orientation != 1) {
					if (grid[pacmanPositionX + 1][pacmanPositionY] == 1) {
						// if there is a wall right to pacman then move to direction
						// convert orientation into direction.
						switch (orientation) {
							case 0 -> Move(2);
							case 2 -> Move(0);
							case 3 -> Move(1);
						}
						break;
					}
				}
					// if there is no wall then move to the next position
					if (grid[pacmanPositionX + 1][pacmanPositionY] != 1) {
						Score(pacmanPositionX + 1, pacmanPositionY);
						ChangePosition(pacmanPositionX + 1, pacmanPositionY);
						orientation = 1;
					}
				}
			}
		}
		for (Ghost ghost : ghosts) {
			if (ghost == null) continue;
			if (ghost.positionX == pacmanPositionX && ghost.positionY == pacmanPositionY) {
				if (ghost.isWeak){
					ghost.Reset();
					score += 100;
				} else {
					Died();
				}
			}
		}
		notifyObservers();
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
		notifyObservers();
	}

	private void Score(int x, int y) {
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

}
