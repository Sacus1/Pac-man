import java.util.ArrayList;
import java.util.List;

public class Astar {
	Node[][] grid;
	private ArrayList<Node> open;
	private ArrayList<Node> closed;

	public Astar(int[][] grid) {
		this.grid = new Node[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				this.grid[i][j] = new Node(i, j);
				this.grid[i][j].isWalkable = (grid[i][j] != 1);
			}
		}
	}

	public void findPath(Node source, Node destination) throws NoPathFoundException {
		open = new ArrayList<>();
		closed = new ArrayList<>();
		open.add(source);
		while (true) {
			Node current = getNodeWithLowestCost();
			if (current.equals(destination))
				return;

			open.remove(current);
			closed.add(current);
			for (Node neighbor : getAdjacent(current)) {
				if (open.contains(neighbor)) {
					// if the neighbor is already in the open list, check if the path to it is shorter
					// if yes, update the previous node, and the costs.
					if (neighbor.gCost > neighbor.calculategCosts(current)) {
						neighbor.setgCosts(current);
						neighbor.previous = current;
					}
				} else {
					open.add(neighbor);
					neighbor.previous = (current);
					neighbor.setgCosts(current);
					neighbor.sethCosts(destination);
				}
			}
			if (open.isEmpty()) {
				throw new NoPathFoundException();
			}
		}
	}

	private List<Node> getAdjacent(Node current) {
		List<Node> neighbors = new ArrayList<>();
		int x = current.x;
		int y = current.y;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i == x && j == y) {
					continue;
				}
				// remove diagonal neighbors
				if (i != x && j != y) {
					continue;
				}
				if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length) {
					if (grid[i][j].isWalkable && !closed.contains(grid[i][j])) {
						neighbors.add(grid[i][j]);
					}
				}
			}
		}
		return neighbors;
	}

	private Node getNodeWithLowestCost() {
		Node best = open.get(0);
		for (Node node : open) {
			if (node.getfCosts() < best.getfCosts()) {
				best = node;
			}
		}
		return best;
	}

	public int[] getNextNode(int[] source, int[] dest) {
		if (dest[0] < 0 || dest[0] >= grid.length || dest[1] < 0 || dest[1] >= grid[0].length) {
			return source;
		}
		try {
			findPath(grid[source[0]][source[1]], grid[dest[0]][dest[1]]);
		} catch (NoPathFoundException e) {
			// search for a node next to the destination
			return getNextNode(source, new int[]{dest[0] + (Math.random() > .5 ? 1 : -1), dest[1] + (Math.random() > .5 ? 1 : -1)});

		}
		List<Node> path = new ArrayList<>();
		Node current = grid[dest[0]][dest[1]];
		while (current != null) {
			path.add(current);
			current = current.previous;
		}
		if (path.size() < 2) return null;
		Node last = path.get(path.size() - 2);
		return new int[]{last.x, last.y};
	}
}

class NoPathFoundException extends Exception {
	public NoPathFoundException() {
		super("No path has been found");
	}
}

class Node {
	public int x, y;
	public boolean isWalkable;
	public Node previous;
	public int gCost;
	public int hCost;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getfCosts() {
		return gCost + hCost;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node node)) return false;
		return node.x == x && node.y == y;
	}

	int calculategCosts(Node current) {
		return (current.gCost + 10);
	}

	void setgCosts(Node current) {
		gCost = calculategCosts(current);
	}

	void sethCosts(Node destination) {
		hCost = Math.abs(x - destination.x) + Math.abs(this.y - destination.y) + 10;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}
}
