import java.util.Random;

public class Blinky extends Ghost {
	public Blinky() {
		positionX = spawnX;
		positionY = spawnY;
	}

	@Override
	public void Move() {
		Model model = Model.getInstance();
		if (!isWeak) {
			// use A* algorithm to find the shortest path to pacman
			Astar shortestPath = new Astar(model.grid);
			int[] newPos = shortestPath.getNextNode(new int[]{positionX, positionY}, new int[]{model.pacmanPositionX,
							model.pacmanPositionY});
			if (newPos == null) return;
			positionX = newPos[0];
			positionY = newPos[1];
			model.notifyObservers();
		} else {
			// if blinky is weak then he will move randomly
			MoveRandomly(model);
		}
	}

}
