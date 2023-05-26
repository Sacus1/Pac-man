import javax.swing.*;
import java.awt.*;

public class View extends JPanel implements Observer {
	public final JFrame frame;
	private static final int CASE_SIZE = 30;
	private final JLabel scoreLabel;
	private final JPanel lifePanel;
	private int[][] grid = new int[10][10];
	/**
	 * 0 : Down
	 * 1 : Right
	 * 2 : Up
	 * 3 : Left
	 */
	private int orientation = 1;
	private int life = 3;
	private boolean start = false;
	private transient Ghost[] ghosts;

	public View() {
		super();
		frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.add(this);
		JPanel panel = new JPanel();
		scoreLabel = new JLabel("Score : 0");
		panel.add(scoreLabel);
		frame.add(panel, BorderLayout.EAST);
		// lifePanel is used to draw the life of the player
		lifePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				for (int i = 0; i < life; i++) {
					g.setColor(Color.YELLOW);
					g.fillArc(10 + i * 30, 10, 20, 20, 30, 300);
				}
			}
		};
		lifePanel.setPreferredSize(new Dimension(100, 100));
		panel.add(lifePanel);
		frame.setVisible(true);
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an {@code Observable} object's
	 * {@code notifyObservers} method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the {@code notifyObservers}
	 *            method.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Model model) {
			grid = model.grid;
			scoreLabel.setText("Score : " + model.getScore());
			switch (model.orientation) {
				case DOWN -> orientation = 0;
				case RIGHT -> orientation = 1;
				case UP -> orientation = 2;
				case LEFT -> orientation = 3;
				default -> throw new IllegalStateException("Unexpected value: " + model.orientation);
			}
			life = model.getLife();
			start = model.start;
			ghosts = model.ghosts;
			if (model.isWin()) {
				JOptionPane.showMessageDialog(frame, "You win !");
				System.exit(0);
			}
			repaint();
			lifePanel.repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!start) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawString("Press any key to start", 100, 300);
			return;
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				g.setColor(Color.gray);
				g.fillRect(CASE_SIZE * i, CASE_SIZE * j, CASE_SIZE, CASE_SIZE);
				switch (grid[i][j]) {
					case 1 -> {
						g.setColor(Color.blue);
						g.fillRect(CASE_SIZE * i, CASE_SIZE * j, CASE_SIZE, CASE_SIZE);
					}
					case 2 -> {
						// pacman
						g.setColor(Color.yellow);
						int x = CASE_SIZE * i + CASE_SIZE / 2;
						int y = CASE_SIZE * j + CASE_SIZE / 2;
						int radius = CASE_SIZE / 2;
						g.fillArc(x - radius, y - radius, radius * 2, radius * 2, 45 + 90 * (orientation - 1), 270);
					}
					case 3 -> {
						g.setColor(Color.yellow);
						g.drawRect(CASE_SIZE * i + CASE_SIZE / 2, CASE_SIZE * j + CASE_SIZE / 2, 1, 1);
					}
					case 4 -> {
						g.setColor(Color.yellow);
						g.fillRect(CASE_SIZE * i + CASE_SIZE / 4, CASE_SIZE * j + CASE_SIZE / 4, CASE_SIZE / 2, CASE_SIZE / 2);
					}
				}
				if (ghosts != null) {
					drawGhost(g, i, j, 0, Color.red);
					drawGhost(g, i, j, 1, Color.pink);
					drawGhost(g, i, j, 2, Color.cyan);
				}

			}
		}
	}

	private void drawGhost(Graphics g, int i, int j, int i2, Color pink) {
		if (ghosts[i2] != null && ghosts[i2].positionX == i && ghosts[i2].positionY == j) {
			g.setColor(ghosts[i2].isWeak ? Color.blue : pink);
			g.fillRoundRect(CASE_SIZE * i + CASE_SIZE / 2 - CASE_SIZE / 4, CASE_SIZE * j + CASE_SIZE / 2 - CASE_SIZE / 4, CASE_SIZE / 2, CASE_SIZE / 2, CASE_SIZE / 4, CASE_SIZE / 4);
		}
	}
}
