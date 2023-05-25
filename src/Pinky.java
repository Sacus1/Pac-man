import java.util.Random;

public class Pinky extends Ghost {
	public Pinky() {
		positionX = spawnX;
		positionY = spawnY;
	}

	@Override
	void Move() {
		Model model = Model.getInstance();
		if (!isWeak) {
			// use A* algorithm to find the shortest path to 2 tiles in front of pacman
			int[] newPos = new int[2];
			switch (model.orientation) {
				case 0 -> newPos = new int[]{model.pacmanPositionX, model.pacmanPositionY - 2};
				case 1 -> newPos = new int[]{model.pacmanPositionX + 2, model.pacmanPositionY};
				case 2 -> newPos = new int[]{model.pacmanPositionX, model.pacmanPositionY + 2};
				case 3 -> newPos = new int[]{model.pacmanPositionX - 2, model.pacmanPositionY};
			}
			Astar shortestPath = new Astar(model.grid);
			newPos = shortestPath.getNextNode(new int[]{positionX, positionY}, newPos);
			if (newPos == null) return;
			positionX = newPos[0];
			positionY = newPos[1];
			model.notifyObservers();
		} else {
			// if pinky is weak then he will move randomly
			MoveRandomly(model);
		}
	}
}
