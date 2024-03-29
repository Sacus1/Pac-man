public class Main {
	public static void main(String[] args) {
		Model model = Model.getInstance();
		View view = new View();
		model.setController(new Controller(model));
		model.addObserver(view);
		model.notifyObservers();
		view.frame.addKeyListener(model.getController());
	}
}
