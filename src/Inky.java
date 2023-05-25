public class Inky extends Ghost {
	    public Inky() {
        positionX = spawnX;
        positionY = spawnY;
    }

    @Override
    void Move() {
        Model model = Model.getInstance();
        if (!isWeak) {
            // get vector from blinky to 2 tiles in front of pacman
            int[] blinkyPos = new int[]{model.ghosts[0].positionX, model.ghosts[0].positionY};
            int[] newPos = new int[2];
            switch (model.orientation) {
                case 0 -> newPos = new int[]{model.pacmanPositionX, model.pacmanPositionY - 2};
                case 1 -> newPos = new int[]{model.pacmanPositionX + 2, model.pacmanPositionY};
                case 2 -> newPos = new int[]{model.pacmanPositionX, model.pacmanPositionY + 2};
                case 3 -> newPos = new int[]{model.pacmanPositionX - 2, model.pacmanPositionY};
            }
            int[] vector = new int[]{newPos[0] - blinkyPos[0], newPos[1] - blinkyPos[1]};
            // double the vector
            vector[0] *= 2;
            vector[1] *= 2;
            // find the path to this new position
            Astar shortestPath = new Astar(model.grid);
            newPos = shortestPath.getNextNode(new int[]{positionX, positionY}, new int[]{blinkyPos[0] + vector[0],
                    blinkyPos[1] + vector[1]});
            if (newPos == null) return;
            positionX = newPos[0];
            positionY = newPos[1];
            model.notifyObservers();
        } else {
            // if inky is weak then he will move randomly
            MoveRandomly(model);
        }
    }
}
