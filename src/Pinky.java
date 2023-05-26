public class Pinky extends Ghost {
	public Pinky() {
		positionX = getSpawnX();
		positionY = getSpawnY();
	}

	@Override
	void move() {
		Model model = Model.getInstance();
		if (!isWeak) {
			// use A* algorithm to find the shortest path to 2 tiles in front of pacman.
			int[] newPos;
			switch (model.orientation) {
				case DOWN -> newPos = new int[]{model.pacmanPositionX, model.pacmanPositionY - 2};
				case RIGHT -> newPos = new int[]{model.pacmanPositionX + 2, model.pacmanPositionY};
				case UP -> newPos = new int[]{model.pacmanPositionX, model.pacmanPositionY + 2};
				case LEFT -> newPos = new int[]{model.pacmanPositionX - 2, model.pacmanPositionY};
				default -> throw new IllegalStateException("Unexpected value: " + model.orientation);
			}
			AStar shortestPath = new AStar(model.grid);
			newPos = shortestPath.getNextNode(new int[]{positionX, positionY}, newPos);
			if (newPos == null) return;
			positionX = newPos[0];
			positionY = newPos[1];
			model.notifyObservers();
		} else {
			// if pinky is weak then he will move randomly
			moveRandomly(model);
		}
	}
}
