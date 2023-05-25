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
		switch (e.getKeyChar()) {
			case 'z' -> model.nextMove = 0;
			case 'q' -> model.nextMove = 1;
			case 's' -> model.nextMove = 2;
			case 'd' -> model.nextMove = 3;
		}
		// if start == false , if any key is pressed, start the game
		if (!start) {
			start = true;
			model.Start();
		}

	}

	/**
	 * Invoked when a key has been pressed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key pressed event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Invoked when a key has been released.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key released event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyReleased(KeyEvent e) {

	}
}
