public class Blinky extends Ghost {
	public Blinky() {
		positionX = getSpawnX();
		positionY = getSpawnY();
	}

	@Override
	public void move() {
		Model model = Model.getInstance();
		if (!isWeak) {
			// use A* algorithm to find the shortest path to pacman
			AStar shortestPath = new AStar(model.grid);
			int[] newPos = shortestPath.getNextNode(new int[]{positionX, positionY}, new int[]{model.pacmanPositionX,
							model.pacmanPositionY});
			if (newPos == null) return;
			positionX = newPos[0];
			positionY = newPos[1];
			model.notifyObservers();
		} else {
			// if blinky is weak then he will move randomly
			moveRandomly(model);
		}
	}

}
