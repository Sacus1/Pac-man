import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Controller implements KeyListener {
	public final Model model;
	private boolean start;
	public Controller(Model model) {
		this.model = model;
	}

	/**
	 * Invoked when a key has been typed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key typed event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// if start == false, if any key is pressed, start the game.
		if (!start) {
			start = true;
			model.start();
		}
		switch (e.getKeyChar()) {
			case 'z' -> model.nextMove = Direction.UP;
			case 'q' -> model.nextMove = Direction.LEFT;
			case 's' -> model.nextMove = Direction.DOWN;
			case 'd' -> model.nextMove = Direction.RIGHT;
			default -> model.nextMove = Direction.NONE; // Stop the player if any other key is pressed
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// ignore
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// ignore
	}

}
